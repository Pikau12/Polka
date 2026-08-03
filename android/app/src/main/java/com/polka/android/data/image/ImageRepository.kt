package com.polka.android.data.image

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import coil3.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject

/**
 * Repository responsible for downloading, persisting, and building display requests for images.
 *
 * ### Threading & Main Safety
 * All suspend operations in this repository are **main-safe** and automatically offload I/O tasks
 * (network requests, disk writes/deletes) to [Dispatchers.IO]. They are safe to call directly from
 * UI threads or ViewModels without block risks.
 *
 * ### Error Handling
 * Methods that perform I/O throw [IOException] upon network errors, HTTP failures, disk space
 * exhaustion, or failed file manipulations.
 */
class ImageRepository @Inject constructor(
    @ApplicationContext private val appContext: Context, private val okHttpClient: OkHttpClient
) {
    /**
     * Directory in app-internal storage where saved images are permanently stored.
     */
    val imagesDir: File by lazy { File(appContext.filesDir, "images").apply { mkdirs() } }

    /**
     * Maps an [ImageSource] into a Coil [ImageRequest] ready to be loaded into UI image views.
     *
     * This function performs no I/O and is safe to call on the main thread.
     *
     * @param imageSource The source representation (either [ImageSource.Saved] or [ImageSource.Unsaved]).
     * @return A configured [ImageRequest] targeting either the saved file or the source URI.
     */
    fun toRequest(imageSource: ImageSource): ImageRequest {
        val data = when (imageSource) {
            is ImageSource.Saved -> File(imagesDir, imageSource.fileName)
            is ImageSource.Unsaved -> imageSource.uri
        }

        return ImageRequest.Builder(appContext).data(data).build()
    }

    /**
     * Persists an [ImageSource] to local app storage.
     *
     * - If [toSave] is already an [ImageSource.Saved], this method returns immediately.
     * - Otherwise, either downloads the remote file via HTTP, or copies the stream from the system [android.content.ContentResolver].
     *   For this, the schema of the [ImageSource.Unsaved.uri] must be either http, https or content.
     *
     * If execution is interrupted, a file with the extension `.tmp` may have been left in the [imagesDir].
     *
     * @param toSave The target image source to persist.
     * @return An [ImageSource.Saved] instance containing the persistent local file name.
     * @throws IOException If the HTTP status is non-200, or disk writing fails.
     */
    suspend fun save(toSave: ImageSource): ImageSource.Saved {
        val uri = when (toSave) {
            is ImageSource.Saved -> return toSave
            is ImageSource.Unsaved -> toSave.uri
        }
        val httpUrl = uri.toString().toHttpUrlOrNull()

        return if (httpUrl == null) {
            saveFromContentResolver(uri)
        } else {
            saveFromNetwork(httpUrl)
        }
    }

    suspend fun save(uri: Uri): ImageSource.Saved = save(ImageSource.Unsaved(uri))

    suspend fun save(url: HttpUrl): ImageSource.Saved = saveFromNetwork(url)

    /**
     * @see save
     */
    suspend fun saveAll(sources: Iterable<ImageSource>): List<ImageSource.Saved> = coroutineScope {
        sources.map { source -> async {save(source)}}.awaitAll()
    }

    /**
     * @see save
     */
    suspend fun saveAll(vararg sources: ImageSource): List<ImageSource.Saved> =
        saveAll(sources.toList())

    /**
     * Deletes a previously saved image from local disk storage.
     * This image source should not be used after the fact.
     *
     * @param saved The saved image reference to delete.
     * @return `true` if the file was successfully deleted; `false` if the file didn't exist or deletion failed.
     */
    suspend fun delete(saved: ImageSource.Saved): Boolean = withContext(Dispatchers.IO) {
        File(imagesDir, saved.fileName).delete()
    }

    /**
     * Clears any temporary files that might have been left if the app exited while saving an image.
     */
    suspend fun clearTempFiles() = withContext(Dispatchers.IO) {
        imagesDir.listFiles { _, name -> name.endsWith(".tmp") }?.forEach { it.delete() }
    }

    private suspend fun saveFromContentResolver(uri: Uri): ImageSource.Saved =
        withContext(Dispatchers.IO) {
            val stream = appContext.contentResolver.openInputStream(uri)
                ?: throw IOException("Could not open input stream for $uri")

            val mimeType = appContext.contentResolver.getType(uri)
            val ext = extractExtension(
                mimeType = mimeType, fallbackPathSegment = uri.lastPathSegment
            )

            val fileName = stream.use { saveStream(it, ext) }
            ImageSource.Saved(fileName)
        }

    private suspend fun saveFromNetwork(httpUrl: HttpUrl): ImageSource.Saved =
        withContext(Dispatchers.IO) {
            val request = Request.Builder().url(httpUrl).build()
            val fileName = okHttpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Failed to download $httpUrl: HTTP ${response.code}")
                }

                val body =
                    response.body ?: throw IOException("Failed to download $httpUrl: body is empty")
                val ext = extractExtension(
                    mimeType = response.header("Content-Type")?.substringBefore(";"),
                    fallbackPathSegment = httpUrl.pathSegments.lastOrNull()
                )

                saveStream(body.byteStream(), ext)
            }
            ImageSource.Saved(fileName)
        }

    private fun saveStream(input: InputStream, ext: String): String {
        val uniqueFileName = "${UUID.randomUUID()}"

        // Saving to temporary file in case app exits mid save
        // Unique temporary file to prevents race conditions during concurrent saves
        val tempFile = File(imagesDir, "$uniqueFileName.tmp")

        try {
            tempFile.outputStream().use { input.copyTo(it) }

            val fileName = "$uniqueFileName.$ext"
            val finalFile = File(imagesDir, fileName)
            if (!tempFile.renameTo(finalFile)) {
                throw IOException("Couldn't rename temporary file")
            }
            return fileName
        } finally {
            if (tempFile.exists()) {
                tempFile.delete()
            }
        }
    }

    private fun extractExtension(
        mimeType: String?, fallbackPathSegment: String?, defaultExt: String = "jpg"
    ): String {
        val fromMime = mimeType?.let { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) }
        if (!fromMime.isNullOrEmpty()) return fromMime

        val fromPath = fallbackPathSegment?.substringAfterLast('.', "")?.takeIf { it.isNotEmpty() }
        return fromPath ?: defaultExt
    }
}
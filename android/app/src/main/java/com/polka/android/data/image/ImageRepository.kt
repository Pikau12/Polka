package com.polka.android.data.image

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import coil3.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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

class ImageRepository @Inject constructor(
    @ApplicationContext private val appContext: Context, private val okHttpClient: OkHttpClient
) {
    val imagesDir: File by lazy { File(appContext.filesDir, "images").apply { mkdirs() } }

    fun toRequest(imageSource: ImageSource): ImageRequest {
        val data = when (imageSource) {
            is ImageSource.Saved -> File(imagesDir, imageSource.fileName)
            is ImageSource.Unsaved -> imageSource.uri
        }

        return ImageRequest.Builder(appContext).data(data).build()
    }

    suspend fun save(toSave: ImageSource): ImageSource.Saved {
        val uri = when (toSave) {
            is ImageSource.Saved -> return toSave
            is ImageSource.Unsaved -> toSave.uri
        }
        val httpUrl = uri.toString().toHttpUrlOrNull()

        return withContext(Dispatchers.IO) {
            val fileName = if (httpUrl == null) {
                saveFromContentResolver(uri)
            } else {
                saveFromNetwork(httpUrl)
            }

            ImageSource.Saved(fileName)
        }
    }

    private fun saveFromContentResolver(uri: Uri): String {
        val stream = appContext.contentResolver.openInputStream(uri)
            ?: throw IOException("Could not open input stream for $uri")

        val mimeType = appContext.contentResolver.getType(uri)
        val ext = extractExtension(
            mimeType = mimeType, fallbackPathSegment = uri.lastPathSegment
        )

        return stream.use { saveStream(it, ext) }
    }

    private fun saveFromNetwork(httpUrl: HttpUrl): String {
        val request = Request.Builder().url(httpUrl).build()
        return okHttpClient.newCall(request).execute().use { response ->
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
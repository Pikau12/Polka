package com.polka.android.data.image

import android.content.Context
import coil3.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.UUID
import javax.inject.Inject

class ImageRepository @Inject constructor(@ApplicationContext val appContext: Context) {
    val imagesDir: File by lazy { File(appContext.filesDir, "images").apply { mkdirs() } }

    fun toRequest(imageSource: ImageSource): ImageRequest {
        val data = when (imageSource) {
            is ImageSource.Saved -> File(imagesDir, imageSource.fileName)
            is ImageSource.Unsaved -> imageSource.uri
        }

        return ImageRequest.Builder(appContext).data(data).build()
    }

    fun save(toSave: ImageSource): ImageSource.Saved {
        val unsaved = toSave as? ImageSource.Unsaved

        // TODO
        return ImageSource.Saved("")
    }

    private fun saveStream(input: InputStream, ext: String): String {
        // Using temporary file in order to not have orphaned files in case app exits mid-saving
        val temp = File(imagesDir, "saving.tmp")
        FileOutputStream(temp, false).use { input.copyTo(it) }

        val fileName = "${UUID.randomUUID()}.$ext"
        val finalFile = File(imagesDir, fileName)
        if (!temp.renameTo(finalFile)) {
            throw IOException("Couldn't rename temporary file to the desired file name")
        }

        return fileName
    }
}
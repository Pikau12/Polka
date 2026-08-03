package com.polka.android.data.image

import android.net.Uri

/**
 * Represents an image that can be displayed by coil library after using `ImageRepository.toRequest`
 */
sealed class ImageSource {
    /**
     * Represents an image saved to the app specific storage and used in the room database.
     * Those are all stored directly in the `images` directory.
     *
     * @param fileName since all the images are stored in the `images` directory, this is just the name of the file, including extension
     */
    class Saved(val fileName: String) : ImageSource()
    /**
     * Represents an image not saved to the app specific storage, therefore not referenced in the room database.
     * Must have either http, https or content scheme.
     *
     * @param uri URI to the image
     */
    class Unsaved(val uri: Uri) : ImageSource()
}


private const val databaseStringPrefix = "content://images/"

fun String.fromDatabaseToSavedImage(): ImageSource.Saved {
    check(this.startsWith(databaseStringPrefix)) { "Database string must start with $databaseStringPrefix" }

    val fileName = this.removePrefix(databaseStringPrefix)

    check(fileName.isNotEmpty()) { "File name cannot be empty" }
    check(!fileName.contains('/')) { "File name cannot contain '/'" }

    return ImageSource.Saved(fileName)
}

fun ImageSource.Saved.toDatabaseString(): String {
    return "$databaseStringPrefix$fileName"
}

package com.polka.android.data.image

import android.net.Uri

/**
 * Represents an image that can be displayed by coil library after using `ImageRepository.toRequest`
 */
sealed class ImageSource {
    /**
     * Represents an image saved to the app specific storage and used in a room database.
     * Those are all stored directly in the `images` directory.
     *
     * @param fileName since all the images are stored in the `images` directory, this is just the name of the file, including extension
     */
    class Saved(val fileName: String) : ImageSource()

    /**
     * Represents an image not saved to the room database.
     *
     * @param uri URI to the image
     */
    class Unsaved(val uri: Uri) : ImageSource()
}

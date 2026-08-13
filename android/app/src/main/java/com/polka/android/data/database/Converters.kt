package com.polka.android.data.database

import androidx.room.TypeConverter
import com.polka.android.data.image.ImageSource
import com.polka.android.data.image.fromDatabaseToSavedImage
import com.polka.android.data.image.toDatabaseString
import com.polka.android.data.model.CollectionItem
import com.polka.android.data.model.toCollectionItemStatus
import org.json.JSONArray
import org.json.JSONException

class Converters {
    @TypeConverter
    fun imageToString(image: ImageSource.Saved?): String? {
        return image?.toDatabaseString()
    }

    @TypeConverter
    fun stringToImage(string: String?): ImageSource.Saved? {
        return string?.fromDatabaseToSavedImage()
    }

    @TypeConverter
    fun imageListToString(images: List<ImageSource.Saved>?): String? {
        if (images == null) {
            return null
        }

        val array = JSONArray()

        for (image in images) {
            array.put(image.fileName)
        }

        return array.toString()
    }

    @TypeConverter
    fun stringToImageList(string: String?): List<ImageSource.Saved>? {
        if (string == null) {
            return null
        }

        val array = try {
            JSONArray(string)
        } catch (e: JSONException) {
            return null
        }

        return try {
            List(array.length()) { i -> ImageSource.Saved(array.getString(i)) }
        } catch (e: JSONException) {
            null
        }
    }

    @TypeConverter
    fun collectionItemStatusToString(status: CollectionItem.Status?): String? {
        return status?.toString()
    }

    @TypeConverter
    fun stringToCollectionItemStatus(string: String?): CollectionItem.Status? {
        return string?.toCollectionItemStatus()
    }
}

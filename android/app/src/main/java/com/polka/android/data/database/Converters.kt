package com.polka.android.data.database

import androidx.room.TypeConverter
import com.polka.android.data.image.ImageSource
import com.polka.android.data.image.fromDatabaseToSavedImage
import com.polka.android.data.image.toDatabaseString
import com.polka.android.data.model.CollectionItem
import com.polka.android.utils.toBitmask
import com.polka.android.utils.toEnumSet
import org.json.JSONArray
import org.json.JSONException
import java.util.EnumSet

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
    fun collectionItemStatusSetToBitmask(status: EnumSet<CollectionItem.Status>?): Long? {
        return status?.toBitmask()
    }

    @TypeConverter
    fun bitmaskToCollectionItemStatusSet(bitmask: Long?): EnumSet<CollectionItem.Status>? {
        return bitmask?.toEnumSet<CollectionItem.Status>()
    }
}

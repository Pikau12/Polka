package com.polka.android.data.database

import androidx.room.TypeConverter
import com.polka.android.data.image.ImageSource
import com.polka.android.data.image.fromDatabaseToSavedImage
import com.polka.android.data.image.toDatabaseString
import com.polka.android.data.model.CollectionItem
import com.polka.android.utils.toBitmask
import com.polka.android.utils.toSet
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

    /**
     * Convert from list to JSON array.
     *
     * @param toPut a function that converts an element of the list to input for [JSONArray.put] function.
     *     A [org.json.JSONObject], [org.json.JSONArray], String, Boolean, Integer, Long, Double,
     *     [org.json.JSONObject.NULL], or `null`. May not be `NaNs` or `infinities`. Unsupported
     *     values are not permitted and will cause the array to be in an inconsistent state.
     */
    fun <T> listToString(list: List<T>?, toPut: (T) -> Any): String? {
        if (list == null) {
            return null
        }
        val array = JSONArray()
        for (item in list) {
            array.put(toPut(item))
        }
        return array.toString()
    }

    /**
     * Convert JSON array to list.
     *
     * @param init - function that extracts an element from the JSON array at specified index.
     */
    fun <T> stringToList(string: String?, init: JSONArray.(i: Int) -> T): List<T>? {
        if (string == null) {
            return null
        }
        val array = try {
            JSONArray(string)
        } catch (e: JSONException) {
            return null
        }
        return try {
            List(array.length()) { i -> array.init(i) }
        } catch (e: JSONException) {
            null
        }
    }

    @TypeConverter
    fun imageListToString(list: List<ImageSource.Saved>?): String? =
        listToString(list) { it.fileName }

    @TypeConverter
    fun stringToImageList(string: String?): List<ImageSource.Saved>? =
        stringToList(string) { i -> ImageSource.Saved(getString(i)) }

    @TypeConverter
    fun stringListToString(list: List<String>?): String? =
        listToString(list) { it }

    @TypeConverter
    fun stringToStringList(string: String?): List<String>? =
        stringToList(string) { i -> getString(i) }

    @TypeConverter
    fun intListToString(list: List<Int>?): String? =
        listToString(list) { it }

    @TypeConverter
    fun stringToIntList(string: String?): List<Int>? =
        stringToList(string) { i -> getInt(i) }

    @TypeConverter
    fun collectionItemStatusSetToBitmask(status: Set<CollectionItem.Status>?): Long? {
        return status?.toBitmask()
    }

    @TypeConverter
    fun bitmaskToCollectionItemStatusSet(bitmask: Long?): Set<CollectionItem.Status>? {
        return bitmask?.toSet<CollectionItem.Status>()
    }
}

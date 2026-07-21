package com.polka.android.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromUserSettings(settings: UserSettings?): String? {
        return settings?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toUserSettings(json: String?): UserSettings? {
        return json?.let {
            val type = object : TypeToken<UserSettings>() {}.type
            Gson().fromJson(it, type)
        }
    }
}
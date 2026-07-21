package com.polka.android.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.polka.android.data.local.database.user.User

@Database(
    entities = [
        User::class,
        LastLoggedInUser::class,
        ],
        version = 2,
        exportSchema = false
)

@TypeConverters(Converters::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

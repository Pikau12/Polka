package com.polka.android.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Model::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun modelDao(): ModelDao
}

package com.polka.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Game::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}

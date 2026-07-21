package com.polka.android.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.polka.android.data.database.model.Game

@Dao
interface GameDao {
    @Insert
    suspend fun insertGame(item: Game): Long
}

package com.polka.android.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.polka.android.data.database.model.GameEntity

@Dao
interface GameDao {
    @Insert
    suspend fun insert(item: GameEntity): Long
}

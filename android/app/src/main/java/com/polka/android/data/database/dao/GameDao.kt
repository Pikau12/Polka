package com.polka.android.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.polka.android.data.database.model.GameEntity

@Dao
interface GameDao {
    @Insert
    suspend fun insert(item: GameEntity): Long

    @Query("SELECT * FROM games WHERE id = :id")
    suspend fun get(id: Long): GameEntity?

    @Query("SELECT * FROM games WHERE bggId = :bggId")
    suspend fun getByBggId(bggId: Long): GameEntity?
}

package com.polka.android.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.polka.android.data.database.model.GameEntity

@Dao
interface GameDao {
    @Insert
    suspend fun insert(item: GameEntity): Long

    /** Returns the number of updated rows (0 if the game does not exist). */
    @Update
    suspend fun update(item: GameEntity): Int

    @Query("SELECT * FROM games WHERE id = :id")
    suspend fun get(id: Long): GameEntity?

    @Query("SELECT * FROM games WHERE bggId = :bggId")
    suspend fun getByBggId(bggId: Long): GameEntity?

    /*
        SQLite compares case-insensitively only for ASCII (COLLATE NOCASE / LOWER),
        so Cyrillic names are compared in Kotlin, see DefaultGameRepository.findGameByName.
     */
    @Query("SELECT * FROM games")
    suspend fun getAll(): List<GameEntity>
}
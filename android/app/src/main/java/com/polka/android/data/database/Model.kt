package com.polka.android.data.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class Game(val name: String) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

// TODO: more entities ...

@Dao
interface GameDao {
    @Insert
    suspend fun insertGame(item: Game)
}

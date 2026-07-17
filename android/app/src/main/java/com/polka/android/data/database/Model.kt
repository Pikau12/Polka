package com.polka.android.data.local.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity
data class Game() {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

@Entity 
data class CollectionItem() {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

// TODO: more entities ...

@Dao
interface GameDao {
    @Insert
    suspend fun insertGame(item: Game)
}

@Dao
interface CollectionDao() {
    @Query("SELECT * FROM collection")
    fun getCollection(): Flow<List<Collection>>
}

// TODO: more Dao's ...


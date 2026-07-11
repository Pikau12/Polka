package com.polka.android.data.local.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity
data class Model(
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

@Dao
interface ModelDao {
    @Query("SELECT * FROM model ORDER BY uid DESC LIMIT 10")
    fun getModels(): Flow<List<Model>>

    @Insert
    suspend fun insertModel(item: Model)
}

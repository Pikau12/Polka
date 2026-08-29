package com.polka.android.data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.polka.android.data.image.ImageSource

@Entity(
    tableName = "games",
    indices = [
        Index(value = ["bggId"], unique = true)
    ]
)
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val serverId: Long?,
    val name: String,
    val shortDescription: String?,
    val image: ImageSource.Saved?,
    val playerCount: List<Int>,
    val bestPlayerCount: List<Int>,
    val minPlayTimeMinutes: Int?,
    val maxPlayTimeMinutes: Int?,
    val ageRestriction: Int?,
    val weight: Double?,
    val designers: List<String>,
    val artists: List<String>,
    val publishers: List<String>,
    val type: List<String>,
    val categories: List<String>,
    val mechanics: List<String>,
    val tags: List<String>,
    val bggId: Long?,
    val bggAverageRating: Double?,
    val polkaAverageRating: Double?,
    val bggNumberOfRatings: Int?,
    val polkaNumberOfRatings: Int?,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
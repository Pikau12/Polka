package com.polka.android.data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "games",
    indices = [
        Index(value = ["bggId"], unique = true)
    ]
)
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val serverId: Long? = null,

    val bggId: Long? = null,

    val name: String,
    val description: String? = null,
    val imageUrl: String? = null,

    val bggRating: Double? = null,
    val polkaRating: Double? = null,
    val ratingsCountAll: Int? = null,
    val ratingsCountPolka: Int? = null,

    /*
        A list of integers.
     */
    val availablePlayerCount: String? = null,
    val bestPlayerCount: String? = null,

    val minPlayTimeMinutes: Int? = null,
    val maxPlayTimeMinutes: Int? = null,

    val minAge: Int? = null,

    val weight: Double? = null,

    val designers: String? = null,
    val artists: String? = null,
    val publishers: String? = null,
    val categories: String? = null,
    val mechanics: String? = null,
    val tags: String? = null,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
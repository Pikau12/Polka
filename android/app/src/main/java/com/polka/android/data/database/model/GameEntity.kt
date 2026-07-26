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
    @PrimaryKey
    val id: Long,

    val bggId: Long? = null,

    val name: String,
    val description: String? = null,
    val imageUrl: String? = null,

    val bggRating: Double? = null,
    val polkaRating: Double? = null,
    val ratingsCountAll: Int? = null,
    val ratingsCountPolka: Int? = null,

    val availableCountPlayers: String, // switch type. Case [2, 4, 6] or [1, 3, 5]
    val bestPlayersCount: String, // switch type. Arrays can't be in entity

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
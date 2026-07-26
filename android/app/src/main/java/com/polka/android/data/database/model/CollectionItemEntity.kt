package com.polka.android.data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "collection_items",
    primaryKeys = ["ownerId", "gameId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE
        ),

        ForeignKey(
            entity = GameEntity::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.CASCADE
        )
    ],

    indices = [
        Index("ownerId"),
        Index("gameId")
    ]
)
data class CollectionItemEntity(
    val gameId: Long,
    val ownerId: Long,

    val status: String? = null,
    val note: String? = null,

    val userRating: Double? = null

    val displayOrder: Int = 0,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
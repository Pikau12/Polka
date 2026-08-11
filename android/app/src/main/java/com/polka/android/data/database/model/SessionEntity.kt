package com.polka.android.data.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sessions",
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.RESTRICT
        ),

        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["creatorId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],

    indices = [
        Index("gameId"),
        Index("creatorId")
    ]
)
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val serverId: Long? = null,

    val gameId: Long,
    val creatorId: Long,

    val note: String = "",

    /*
     *  A list of URL to images.
     *  TODO: when implementing utility for list conversion, replace this with an empty list from that utility.
     */
    val images: String = "[]",

    val startedAt: Long = System.currentTimeMillis(),
    val finishedAt: Long? = null,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
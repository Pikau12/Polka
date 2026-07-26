package com.polka.android.data.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "session_participants",
    foreignKeys = [
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        ),

        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],

    indices = [
        Index(value = ["sessionId"]),
        Index(value = ["userId"]),
        Index(
            value = ["sessionId", "userId"],
            unique = true
        )
    ]
)
data class SessionParticipantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long,
    /*
        Name must be not null when userId is null.
     */
    val name: String?,
    val userId: Long?,

    val isWinner: Boolean = false,
    val score: Double? = null,
)

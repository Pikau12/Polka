package com.polka.android.data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["login"], unique = true),
    ]
)

data class UserEntity (
    @PrimaryKey
    val id: Long,

    val username: String? = null,
    val login: String = null,

    val avatarUrl: String? = null,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
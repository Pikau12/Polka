package com.polka.android.data.database.user

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String,
    val foreignId: String,
    val username: String,
    val email: String,
    val passwordHash: String,
    val login: String,
    val isBggSynced: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val avatar: String? = null,
    val userSettings: String? = null
)
package com.polka.android.data.model

data class User(
    val id: Long,

    val login: String,
    val username: String,

    val avatarUrl: String?,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
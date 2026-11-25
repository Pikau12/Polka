package com.polka.android.data.network.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val login: String,
    val password: String,
    val email: String,
    val username: String
)
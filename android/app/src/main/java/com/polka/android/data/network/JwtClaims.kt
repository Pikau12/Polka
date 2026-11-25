package com.polka.android.data.network

import android.util.Base64
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal object JwtClaims {
    private val USER_ID_CLAIMS = listOf("user_id", "uid", "sub", "id")
    private val json = Json { ignoreUnknownKeys = true }

    fun userId(token: String): Long? {
        val payload = token.split('.').getOrNull(1) ?: return null
        return try {
            val bytes = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
            val claims = json.parseToJsonElement(String(bytes, Charsets.UTF_8)).jsonObject
            USER_ID_CLAIMS.firstNotNullOfOrNull { claims[it]?.jsonPrimitive?.contentOrNull?.toLongOrNull() }
        } catch (e: Exception) {
            null
        }
    }
}
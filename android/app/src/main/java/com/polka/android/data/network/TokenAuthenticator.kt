package com.polka.android.data.network

import com.polka.android.data.api.AuthApi
import com.polka.android.data.network.dto.auth.RefreshRequest
import com.polka.android.data.preferences.AuthPreferences
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.HttpException
import java.io.IOException

@Singleton
class TokenAuthenticator @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val authApi: AuthApi
) : Authenticator {

    private val lock = Any()

    override fun authenticate(route: Route?, response: Response): Request? {
        val sentHeader = response.request.header("Authorization") ?: return null
        if (response.attempts() >= 2) return null

        synchronized(lock) {
            val sentToken = sentHeader.removePrefix("Bearer ")
            val currentToken = runBlocking { authPreferences.getAccessToken() }

            if (currentToken != null && currentToken != sentToken) {
                return response.request.withToken(currentToken)
            }

            val refreshToken = runBlocking { authPreferences.getRefreshToken() } ?: return null

            val refreshed = try {
                runBlocking { authApi.refresh(RefreshRequest(refreshToken)) }
            } catch (e: HttpException) {
                if (e.code() == 400 || e.code() == 401) {
                    runBlocking { authPreferences.clearTokens() }
                }
                return null
            } catch (e: IOException) {
                return null
            }

            runBlocking { authPreferences.saveTokens(refreshed.accessToken, refreshed.refreshToken) }
            return response.request.withToken(refreshed.accessToken)
        }
    }

    private fun Request.withToken(token: String): Request =
        newBuilder().header("Authorization", "Bearer $token").build()

    private fun Response.attempts(): Int {
        var count = 1
        var prior = priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}
package com.polka.android.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class AuthPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    suspend fun saveSession(accessToken: String, refreshToken: String, userId: Long) {
        dataStore.edit {
            it[ACCESS_TOKEN] = accessToken
            it[REFRESH_TOKEN] = refreshToken
            it[USER_ID] = userId
        }
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit {
            it[ACCESS_TOKEN] = accessToken
            it[REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun clearTokens() {
        dataStore.edit {
            it.remove(ACCESS_TOKEN)
            it.remove(REFRESH_TOKEN)
        }
    }

    suspend fun clearSession() {
        dataStore.edit {
            it.remove(ACCESS_TOKEN)
            it.remove(REFRESH_TOKEN)
            it.remove(USER_ID)
        }
    }

    suspend fun getAccessToken(): String? = dataStore.data.first()[ACCESS_TOKEN]
    suspend fun getRefreshToken(): String? = dataStore.data.first()[REFRESH_TOKEN]
    suspend fun getUserId(): Long? = dataStore.data.first()[USER_ID]

    suspend fun isOnboardingSeen(): Boolean = dataStore.data.first()[ONBOARDING_SEEN] ?: false
    suspend fun markOnboardingSeen() {
        dataStore.edit { it[ONBOARDING_SEEN] = true }
    }

    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val USER_ID = longPreferencesKey("user_id")
        val ONBOARDING_SEEN = booleanPreferencesKey("onboarding_seen")
    }
}
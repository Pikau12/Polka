package com.polka.android.data

import jakarta.inject.Inject

interface AuthRepository {

    suspend fun login(login: String, password: String): Result<User>
    suspend fun register(login: String, username: String, password: String): Result<User>
    suspend fun logout()
    fun isBggAccountLinked(userId: Long): Flow<Boolean>
    suspend fun getUserOffline(): User?
    suspend fun validatePasswordLocally(password: String): Boolean
    fun shouldShowOnboarding(): Flow<Boolean>
    suspend fun markOnboardingAsSeen()
}

class DefaultAuthRepository @Inject constructor() : AuthRepository {
}

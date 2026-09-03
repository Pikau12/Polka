package com.polka.android.data

import com.polka.android.data.model.User
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun login(login: String, password: String): Result<User>
    suspend fun register(login: String, username: String, password: String): Result<User>
    suspend fun logout()
    fun isBggAccountLinked(userId: Long): Flow<Boolean>
    suspend fun validatePasswordLocally(password: String): String
    fun shouldShowOnboarding(): Boolean
    suspend fun markOnboardingAsSeen()
    fun shouldShowLoginScreen(): Boolean
    fun getCurrentUser(): User?

}

class DefaultAuthRepository @Inject constructor() : AuthRepository {
    override suspend fun login(login: String, password: String): Result<User> {
        TODO("Not implemented yet")
    }

    override suspend fun register(login: String, username: String, password: String): Result<User> {
        TODO("Not implemented yet")
    }

    override suspend fun logout() {
        TODO("Not implemented yet")
    }

    override fun isBggAccountLinked(userId: Long): Flow<Boolean> {
        TODO("Not implemented yet")
    }

    override suspend fun validatePasswordLocally(password: String): String {
        TODO("Not implemented yet")
    }

    override fun shouldShowOnboarding(): Boolean {
        TODO("Not implemented yet")
    }

    override suspend fun markOnboardingAsSeen() {
        TODO("Not implemented yet")
    }

    override fun shouldShowLoginScreen(): Boolean {
        TODO("Not implemented yet")
    }

    override fun getCurrentUser(): User {
        TODO("Not implemented yet")
    }
}

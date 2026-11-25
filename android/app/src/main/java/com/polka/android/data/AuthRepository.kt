package com.polka.android.data

import com.polka.android.data.api.AuthApi
import com.polka.android.data.model.User
import com.polka.android.data.network.AuthException
import com.polka.android.data.network.JwtClaims
import com.polka.android.data.network.dto.auth.LoginRequest
import com.polka.android.data.network.dto.auth.LogoutRequest
import com.polka.android.data.network.dto.auth.RegisterRequest
import com.polka.android.data.network.toAuthException
import com.polka.android.data.preferences.AuthPreferences
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first

interface AuthRepository {
    suspend fun login(login: String, password: String): Result<Unit>
    suspend fun register(login: String, username: String, email: String, password: String): Result<Unit>
    suspend fun logout()
    fun isBggAccountLinked(userId: Long): Flow<Boolean>
    suspend fun validatePasswordLocally(password: String): String
    suspend fun shouldShowOnboarding(): Boolean
    suspend fun markOnboardingAsSeen()
    suspend fun shouldShowLoginScreen(): Boolean

    /**
     * Id of the logged-in user, `null` if nobody is logged in.
     * Emits again on login, logout and account switch: use it for data that must follow the account.
     */
    val currentUserId: Flow<Long?>

    /** Throws [AuthException.NotAuthenticated] if nobody is logged in. */
    suspend fun getCurrentUser(): User
}

class DefaultAuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val authPreferences: AuthPreferences,
    private val userRepository: UserRepository
) : AuthRepository {

    override suspend fun login(login: String, password: String): Result<Unit> = authCall {
        val response = authApi.login(LoginRequest(login, password))

        val userId = JwtClaims.userId(response.accessToken)
            ?: throw AuthException.Unknown("Cannot determine user id from the access token")

        val existing = userRepository.observeUser(userId).first()
        val user = existing?.copy(login = login, updatedAt = System.currentTimeMillis())
            ?: User(id = userId, login = login, username = login, avatarUrl = null)

        userRepository.saveUser(user)
        authPreferences.saveSession(response.accessToken, response.refreshToken, userId)
    }

    override suspend fun register(
        login: String,
        username: String,
        email: String,
        password: String
    ): Result<Unit> = authCall {
        authApi.register(RegisterRequest(login = login, password = password, email = email, username = username))
    }

    override suspend fun logout() {
        val refreshToken = authPreferences.getRefreshToken()
        if (refreshToken != null) {
            try {
                authApi.logout(LogoutRequest(refreshToken))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
            }
        }
        authPreferences.clearSession()
    }

    override fun isBggAccountLinked(userId: Long): Flow<Boolean> {
        TODO("Not implemented yet")
    }

    override suspend fun validatePasswordLocally(password: String): String {
        if (password.length < 8) return "Пароль должен содержать минимум 8 символов"
        if (password.none { it.isDigit() }) return "Пароль должен содержать цифру"
        return ""
    }

    override suspend fun shouldShowOnboarding(): Boolean = !authPreferences.isOnboardingSeen()

    override suspend fun markOnboardingAsSeen() {
        authPreferences.markOnboardingSeen()
    }

    override suspend fun shouldShowLoginScreen(): Boolean = authPreferences.getRefreshToken() == null

    override val currentUserId: Flow<Long?> = authPreferences.userIdFlow.distinctUntilChanged()

    override suspend fun getCurrentUser(): User {
        return authPreferences.getUserId()?.let { userRepository.observeUser(it).first() }
            ?: throw AuthException.NotAuthenticated()
    }

    private suspend inline fun <T> authCall(block: () -> T): Result<T> = try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e.toAuthException())
    }
}
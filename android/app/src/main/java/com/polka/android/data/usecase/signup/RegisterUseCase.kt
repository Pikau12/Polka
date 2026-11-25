package com.polka.android.data.usecase.signup

import com.polka.android.data.AuthRepository
import jakarta.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(login: String, username: String, email: String, password: String): Result<Unit> =
        authRepository.register(login, username, email, password)
}
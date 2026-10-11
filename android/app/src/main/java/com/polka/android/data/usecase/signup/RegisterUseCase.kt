package com.polka.android.data.usecase.signup

import com.polka.android.data.AuthRepository
import com.polka.android.data.model.User
import jakarta.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(login: String, username: String, password: String): Result<User> {
        return authRepository.register(login, username, password)
    }
}
package com.polka.android.data.usecase.login

import com.polka.android.data.AuthRepository
import com.polka.android.data.model.User
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(login: String, password: String): Result<User> {
        return authRepository.login(login, password)
    }
}
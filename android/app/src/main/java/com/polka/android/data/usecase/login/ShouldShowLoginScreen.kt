package com.polka.android.data.usecase.login

import com.polka.android.data.AuthRepository
import javax.inject.Inject

class ShouldShowLoginScreen @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() : Boolean {
        return authRepository.shouldShowLoginScreen()
    }
}
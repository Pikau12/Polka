package com.polka.android.data.usecase.login

import com.polka.android.data.AuthRepository
import javax.inject.Inject

class ShouldShowOnboarding @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() : Boolean {
        return authRepository.shouldShowOnboarding()
    }
}
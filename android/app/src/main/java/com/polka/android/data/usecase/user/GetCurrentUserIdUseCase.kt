package com.polka.android.data.usecase.user

import com.polka.android.data.AuthRepository
import jakarta.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor (
    private val authRepository: AuthRepository
) {
    operator fun invoke (): Long{
        return authRepository.getCurrentUser().id
    }
}
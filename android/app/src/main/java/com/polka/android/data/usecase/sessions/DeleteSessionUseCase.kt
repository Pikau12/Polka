package com.polka.android.data.usecase.sessions

import com.polka.android.data.AuthRepository
import com.polka.android.data.SessionRepository
import jakarta.inject.Inject

/**
 * Deletes a session. For now only the creator may delete it.
 * Removing yourself from someone else's session is a separate feature.
 */
class DeleteSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke(sessionId: Long) {
        val existing = sessionRepository.getSessionById(sessionId) ?: return
        check(existing.creatorId == authRepository.getCurrentUser().id) {
            "Only the creator can delete a session"
        }
        sessionRepository.deleteSessionById(sessionId)
    }
}
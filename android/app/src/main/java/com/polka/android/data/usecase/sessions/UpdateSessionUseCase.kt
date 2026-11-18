package com.polka.android.data.usecase.sessions

import com.polka.android.data.AuthRepository
import com.polka.android.data.GameRepository
import com.polka.android.data.SessionRepository
import com.polka.android.data.model.SessionParticipant
import jakarta.inject.Inject

/**
 * Edits a session. Only the creator of the session may edit it.
 * The game and the creator of a session cannot be changed.
 */
class UpdateSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val gameRepository: GameRepository,
    private val sessionRepository: SessionRepository,
    private val validator: SessionValidator,
) {
    suspend operator fun invoke(
        sessionId: Long,
        startedAt: Long,
        finishedAt: Long?,
        note: String,
        participants: List<SessionParticipant>,
    ) {
        val existing = sessionRepository.getSessionById(sessionId)
            ?: error("Session $sessionId does not exist")
        check(existing.creatorId == authRepository.getCurrentUser().id) {
            "Only the creator can edit a session"
        }

        val game = gameRepository.getGame(existing.gameId)
            ?: error("Game ${existing.gameId} does not exist")
        validator.validate(game, startedAt, finishedAt, participants)

        sessionRepository.updateSession(
            existing.copy(startedAt = startedAt, finishedAt = finishedAt, note = note),
            participants,
        )
    }
}
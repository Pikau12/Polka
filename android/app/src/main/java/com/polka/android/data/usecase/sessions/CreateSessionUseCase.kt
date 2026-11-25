package com.polka.android.data.usecase.sessions

import com.polka.android.data.AuthRepository
import com.polka.android.data.GameRepository
import com.polka.android.data.SessionRepository
import com.polka.android.data.model.Session
import com.polka.android.data.model.SessionParticipant
import jakarta.inject.Inject

class CreateSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val gameRepository: GameRepository,
    private val sessionRepository: SessionRepository,
    private val validator: SessionValidator,
) {
    suspend operator fun invoke(
        gameId: Long,
        startedAt: Long,
        finishedAt: Long?,
        note: String,
        participants: List<SessionParticipant>,
        place: String? = null,
        status: Session.Status = Session.Status.FINISHED,
    ): Long {
        val game = gameRepository.getGame(gameId) ?: error("Game $gameId does not exist")
        validator.validate(game, startedAt, finishedAt, status, participants)

        val session = Session(
            gameId = gameId,
            creatorId = authRepository.getCurrentUser().id,
            note = note,
            startedAt = startedAt,
            finishedAt = finishedAt,
            place = place?.trim()?.takeIf { it.isNotEmpty() },
            status = status,
        )
        return sessionRepository.createSession(session, participants)
    }
}
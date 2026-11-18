package com.polka.android.data.usecase.sessions

import com.polka.android.data.model.Game
import com.polka.android.data.model.SessionParticipant
import jakarta.inject.Inject

/**
 * Minimal rules a session must satisfy, shared by creation and editing.
 */
class SessionValidator @Inject constructor() {
    fun validate(
        game: Game,
        startedAt: Long,
        finishedAt: Long?,
        participants: List<SessionParticipant>,
    ) {
        require(participants.isNotEmpty()) { "A session must have at least one player" }
        require(participants.any { it.isWinner }) { "A session must have a winner" }
        require(finishedAt == null || finishedAt >= startedAt) {
            "Session cannot finish before it starts"
        }

        val maxPlayers = game.playerCount?.maxOrNull()
        require(maxPlayers == null || participants.size <= maxPlayers) {
            "The game supports at most $maxPlayers players"
        }
    }
}
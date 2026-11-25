package com.polka.android.data.usecase.sessions

import com.polka.android.data.model.Game
import com.polka.android.data.model.Session
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
        status: Session.Status,
        participants: List<SessionParticipant>,
    ) {
        require(participants.isNotEmpty()) { "A session must have at least one player" }
        require(status != Session.Status.FINISHED || participants.any { it.isWinner }) {
            "A finished session must have a winner"
        }
        require(status == Session.Status.FINISHED || finishedAt == null) {
            "Only a finished session can have an end time"
        }
        require(finishedAt == null || finishedAt >= startedAt) {
            "Session cannot finish before it starts"
        }

        // Players who declined are not counted: they are shown as plain names.
        val playing = participants.count { it.status != SessionParticipant.Status.DECLINED }
        val maxPlayers = game.playerCount?.maxOrNull()
        require(maxPlayers == null || playing <= maxPlayers) {
            "The game supports at most $maxPlayers players"
        }
    }
}
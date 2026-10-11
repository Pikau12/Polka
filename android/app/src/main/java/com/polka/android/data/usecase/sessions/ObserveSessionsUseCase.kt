package com.polka.android.data.usecase.sessions

import com.polka.android.data.AuthRepository
import com.polka.android.data.GameRepository
import com.polka.android.data.SessionRepository
import com.polka.android.data.model.Game
import com.polka.android.data.model.Session
import com.polka.android.presentation.model.SessionSummary
import com.polka.android.utils.toLocalDate
import com.polka.android.utils.toUiString
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveSessionsUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    private val gameRepository: GameRepository,
    private val mapper: SessionMapper,
) {
    operator fun invoke() : Flow<List<SessionSummary>> {
        return sessionRepository.getUserSessions(authRepository.getCurrentUser().id)
                .map { items ->
                    items.map { session ->
                        val game = gameRepository.getGame(session.gameId)
                            ?: error("Game (id: ${session.gameId} not found in observeSessionsUseCase)")
                        mapper.map(session, game)
                    }
                }
    }
}

class SessionMapper @Inject constructor() {
    fun map(
        session: Session,
        game: Game
    ): SessionSummary {
        val duration = session.finishedAt?.minus(session.startedAt)

        return SessionSummary(
            sessionId = session.id,
            gameName = game.name,
            gameImage = game.gameImage,
            date = session.startedAt.toLocalDate().toUiString(),
            duration = duration?.let { if (duration % 60 == 0L) (duration / 60f).toInt().toString() + " h"
            else if (duration % 30 == 0L) (duration / 60f).toString() + " h"
            else "$duration m"
            },
            place = "PLACEPLACEHOLDER", // TODO
            players = listOf("PLAYERPLACEHOLDER1", "PLAYERPLACEHOLDER2"), // TODO
            winners = mapOf(Pair("PLAYERPLACEHOLDER1", true), Pair("PLAYERPLACEHOLDER2", false)) // TODO
        )
    }
}
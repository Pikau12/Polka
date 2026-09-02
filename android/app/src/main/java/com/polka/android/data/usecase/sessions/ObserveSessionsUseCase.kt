package com.polka.android.data.usecase.sessions

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.polka.android.data.AuthRepository
import com.polka.android.data.DefaultSessionRepository
import com.polka.android.data.GameRepository
import com.polka.android.data.SessionRepository
import com.polka.android.data.image.ImageRepository
import com.polka.android.data.model.Game
import com.polka.android.data.model.Session
import com.polka.android.presentation.model.CollectionItem
import com.polka.android.presentation.model.SessionSummary
import com.polka.android.presentation.model.toUIStatusSet
import com.polka.android.utils.toLocalDate
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

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

class SessionMapper @Inject constructor(
    private val imageRepository: ImageRepository
) {
    fun map(
        session: Session,
        game: Game
    ): SessionSummary {
        return SessionSummary(
            sessionId = session.id,
            gameName = game.name,
            gameImage = game.image?.let { imageRepository.toRequest(it) },
            date = session.startedAt.toLocalDate(),
            duration = session.finishedAt?.minus(session.startedAt), // TODO
            place = "PLACEPLACEHOLDER", // TODO
            players = listOf("PLAYERPLACEHOLDER1", "PLAYERPLACEHOLDER2"), // TODO
            winners = mapOf(Pair("PLAYERPLACEHOLDER1", true), Pair("PLAYERPLACEHOLDER2", false)) // TODO
        )
    }
}
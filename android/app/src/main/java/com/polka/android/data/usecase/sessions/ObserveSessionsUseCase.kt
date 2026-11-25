package com.polka.android.data.usecase.sessions

import com.polka.android.data.AuthRepository
import com.polka.android.data.GameRepository
import com.polka.android.data.SessionRepository
import com.polka.android.data.UserRepository
import com.polka.android.data.image.ImageRepository
import com.polka.android.data.model.Game
import com.polka.android.data.model.Session
import com.polka.android.data.model.SessionParticipant
import com.polka.android.presentation.model.PlayerStatus
import com.polka.android.presentation.model.SessionSummary
import com.polka.android.utils.toLocalDate
import com.polka.android.utils.toUiString
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveSessionsUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
    private val mapper: SessionMapper,
) {
    operator fun invoke(): Flow<List<SessionSummary>> {
        return authRepository.currentUserId
            .filterNotNull()
            .flatMapLatest { userId -> sessionRepository.getUserSessions(userId) }
            .map { items ->
                items.map { session ->
                    val game = gameRepository.getGame(session.gameId)
                        ?: error("Game (id: ${session.gameId}) not found in ObserveSessionsUseCase")
                    val participants = sessionRepository.getParticipants(session.id)
                    val names = participants.associateWith { displayName(it) }
                    mapper.map(session, game, names)
                }
            }
    }

    private suspend fun displayName(participant: SessionParticipant): String {
        val user = participant.userId?.let { userRepository.getUser(it) }
        return user?.username ?: participant.name ?: "?"
    }
}

class SessionMapper @Inject constructor(
    private val imageRepository: ImageRepository,
) {
    fun map(
        session: Session,
        game: Game,
        participantNames: Map<SessionParticipant, String>,
    ): SessionSummary {
        return SessionSummary(
            sessionId = session.id,
            gameName = game.name,
            gameImage = game.gameImage?.let(imageRepository::toRequest),
            date = session.startedAt.toLocalDate().toUiString(),
            duration = session.finishedAt?.let { formatDuration(it - session.startedAt) },
            place = session.place,
            players = participantNames.values.toList(),
            winners = participantNames.entries.associate { (participant, name) ->
                name to participant.isWinner
            },
            playersStatus = participantNames.entries.associate { (participant, name) ->
                name to PlayerStatus.valueOf(participant.status.name)
            },
        )
    }

    /** [millis] is a difference of two timestamps in milliseconds. */
    private fun formatDuration(millis: Long): String {
        val minutes = millis / 60_000
        return when {
            minutes < 60 -> "$minutes m"
            minutes % 60 == 0L -> "${minutes / 60} h"
            else -> "${minutes / 60} h ${minutes % 60} m"
        }
    }
}
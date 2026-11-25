package com.polka.android.data

import com.polka.android.data.database.dao.SessionDao
import com.polka.android.data.mapper.toEntity
import com.polka.android.data.mapper.toModel
import com.polka.android.data.model.Session
import com.polka.android.data.model.SessionParticipant
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SessionRepository {
    /** Saves a new session together with its participants. @return id of the new session. */
    suspend fun createSession(session: Session, participants: List<SessionParticipant>): Long

    /** Updates the session and replaces the whole list of its participants. */
    suspend fun updateSession(session: Session, participants: List<SessionParticipant>)

    suspend fun deleteSessionById(sessionId: Long)
    fun getUserSessions(userId: Long): Flow<List<Session>>
    fun getSessionsByGameId(gameId: Long): Flow<List<Session>>
    suspend fun getSessionById(sessionId: Long): Session?
    suspend fun getParticipants(sessionId: Long): List<SessionParticipant>
}

class DefaultSessionRepository @Inject constructor(
    private val sessionDao: SessionDao
) : SessionRepository {
    override suspend fun createSession(
        session: Session,
        participants: List<SessionParticipant>,
    ): Long {
        val now = System.currentTimeMillis()
        return sessionDao.createSessionWithParticipants(
            session.copy(id = 0, createdAt = now, updatedAt = now).toEntity(),
            participants.map { it.toEntity() },
        )
    }

    override suspend fun updateSession(
        session: Session,
        participants: List<SessionParticipant>,
    ) {
        sessionDao.updateSessionWithParticipants(
            session.copy(updatedAt = System.currentTimeMillis()).toEntity(),
            participants.map { it.toEntity() },
        )
    }

    override suspend fun deleteSessionById(sessionId: Long) {
        sessionDao.deleteSessionById(sessionId)
    }

    override fun getUserSessions(userId: Long): Flow<List<Session>> {
        return sessionDao.getUserSessions(userId)
            .map { entities -> entities.map { entity -> entity.toModel() } }
    }

    override fun getSessionsByGameId(gameId: Long): Flow<List<Session>> {
        return sessionDao.getSessionsByGameId(gameId)
            .map { entities -> entities.map { entity -> entity.toModel() } }
    }

    override suspend fun getSessionById(sessionId: Long): Session? {
        return sessionDao.getSessionById(sessionId)?.toModel()
    }

    override suspend fun getParticipants(sessionId: Long): List<SessionParticipant> {
        return sessionDao.getParticipants(sessionId).map { it.toModel() }
    }
}
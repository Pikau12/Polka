package com.polka.android.data

import com.polka.android.data.database.dao.SessionDao
import com.polka.android.data.mapper.toEntity
import com.polka.android.data.mapper.toModel
import com.polka.android.data.model.Session
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SessionRepository {
    suspend fun saveSession(session: Session)
    suspend fun deleteSessionById(sessionId: Long)
    suspend fun updateSession(session: Session)
    fun getUserSessions(userId: Long): Flow<List<Session>>
    fun getSessionsByGameId(gameId: Long): Flow<List<Session>>
    suspend fun getSessionById(sessionId: Long): Session?
}

class DefaultSessionRepository @Inject constructor(
    private val sessionDao: SessionDao
) : SessionRepository {
    override suspend fun saveSession(session: Session) {
        sessionDao.saveSession(
            session.copy(updatedAt = System.currentTimeMillis()).toEntity()
        )
    }

    override suspend fun deleteSessionById(sessionId: Long) {
        sessionDao.deleteSessionById(sessionId)
    }

    override suspend fun updateSession(session: Session) {
        sessionDao.updateSession(
            session.copy(updatedAt = System.currentTimeMillis()).toEntity()
        )
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

    /*
    fun getPendingSyncSessions(): Flow<List<Session>> {
        return sessionDao.getPendingSyncSessions()
    }

    suspend fun markSessionAsSynced(sessionId: Long) {

    */
}

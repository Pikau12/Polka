package com.polka.android.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.polka.android.data.database.model.SessionEntity
import com.polka.android.data.database.model.SessionParticipantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert
    suspend fun insertSession(session: SessionEntity): Long

    @Update
    suspend fun updateSession(session: SessionEntity)

    @Insert
    suspend fun insertParticipants(participants: List<SessionParticipantEntity>)

    @Query("DELETE FROM session_participants WHERE sessionId = :sessionId")
    suspend fun deleteParticipants(sessionId: Long)

    @Query("SELECT * FROM session_participants WHERE sessionId = :sessionId ORDER BY id ASC")
    suspend fun getParticipants(sessionId: Long): List<SessionParticipantEntity>

    /**
     * Saves the session together with its participants in one transaction.
     * @return the id of the created session.
     */
    @Transaction
    suspend fun createSessionWithParticipants(
        session: SessionEntity,
        participants: List<SessionParticipantEntity>,
    ): Long {
        val sessionId = insertSession(session)
        insertParticipants(participants.map { it.copy(id = 0, sessionId = sessionId) })
        return sessionId
    }

    /**
     * Updates the session and replaces the whole list of its participants in one transaction.
     */
    @Transaction
    suspend fun updateSessionWithParticipants(
        session: SessionEntity,
        participants: List<SessionParticipantEntity>,
    ) {
        updateSession(session)
        deleteParticipants(session.id)
        insertParticipants(participants.map { it.copy(id = 0, sessionId = session.id) })
    }

    /**
     * Sessions created by the user or where the user is a participant.
     */
    @Query("""
        SELECT DISTINCT sessions.* FROM sessions
        LEFT JOIN session_participants ON session_participants.sessionId = sessions.id
        WHERE sessions.creatorId = :userId OR session_participants.userId = :userId
        ORDER BY sessions.startedAt DESC
    """)
    fun getUserSessions(userId: Long): Flow<List<SessionEntity>>

    @Query("""
        SELECT * FROM sessions
        WHERE sessions.gameId = :gameId
        ORDER BY sessions.startedAt DESC
    """)
    fun getSessionsByGameId(gameId: Long): Flow<List<SessionEntity>>

    @Query("""
        SELECT * FROM sessions
        WHERE sessions.id = :sessionId
    """)
    suspend fun getSessionById(sessionId: Long): SessionEntity?

    @Query("""
        DELETE FROM sessions
        WHERE sessions.id = :sessionId
    """)
    suspend fun deleteSessionById(sessionId: Long)
}
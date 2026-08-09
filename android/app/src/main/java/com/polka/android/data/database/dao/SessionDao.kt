package com.polka.android.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.polka.android.data.database.model.SessionEntity
import com.polka.android.data.database.model.SessionParticipantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Upsert
    suspend fun saveSession(session: SessionEntity)

    @Update
    suspend fun updateSession(session: SessionEntity)

    @Query("""
        SELECT sessions.* FROM sessions
        INNER JOIN session_participants ON session_participants.sessionId = sessions.id
        WHERE session_participants.userId = :userId
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

    /*
    @Query("""
    SELECT * FROM sessions
    WHERE ...
    """)
    fun getPendingSyncSessions(): Flow<List<SessionEntity>>
     */

    /*
    @Query("""
    SELECT * FROM sessions
    WHERE sessions.sessionId = :sessionId
    """)
    suspend fun markSessionAsSynced(sessionId: Long)
    */
}
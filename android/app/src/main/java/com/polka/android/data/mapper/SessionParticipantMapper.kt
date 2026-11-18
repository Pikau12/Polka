package com.polka.android.data.mapper

import com.polka.android.data.database.model.SessionParticipantEntity
import com.polka.android.data.model.SessionParticipant

fun SessionParticipantEntity.toModel(): SessionParticipant {
    return SessionParticipant(
        id = id,
        sessionId = sessionId,
        userId = userId,
        name = name,
        isWinner = isWinner,
        score = score,
    )
}

fun SessionParticipant.toEntity(): SessionParticipantEntity {
    return SessionParticipantEntity(
        id = id,
        sessionId = sessionId,
        userId = userId,
        name = name?.trim(),
        isWinner = isWinner,
        score = score,
    )
}
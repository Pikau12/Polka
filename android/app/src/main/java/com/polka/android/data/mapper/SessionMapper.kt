package com.polka.android.data.mapper

import com.polka.android.data.database.model.SessionEntity
import com.polka.android.data.model.Session

fun SessionEntity.toModel(): Session {
    return Session(
        id = id,
        gameId = gameId,
        creatorId = creatorId,
        note = note,
        images = images,
        startedAt = startedAt,
        finishedAt = finishedAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Session.toEntity(): SessionEntity {
    return SessionEntity(
        id = id,
        gameId = gameId,
        creatorId = creatorId,
        note = note,
        images = images,
        startedAt = startedAt,
        finishedAt = finishedAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
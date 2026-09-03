package com.polka.android.data.mapper

import com.polka.android.data.database.model.UserEntity
import com.polka.android.data.model.User

fun UserEntity.toModel(): User {
    return User(
        id = id,
        login = login,
        username = username,
        avatarUrl = avatarUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        login = login,
        username = username,
        avatarUrl = avatarUrl,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
package com.polka.android.data

import com.polka.android.data.database.dao.UserDao
import com.polka.android.data.mapper.toEntity
import com.polka.android.data.mapper.toModel
import com.polka.android.data.model.User
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UserRepository {

    fun observeUser(userId: Long): Flow<User?>

    suspend fun saveUser(user: User)

    suspend fun updateUser(user: User)

    suspend fun updateUsername(
        userId: Long,
        username: String
    )

    suspend fun updateLogin(
        userId: Long,
        login: String
    )

    suspend fun updateAvatar(
        userId: Long,
        avatarUrl: String?
    )

    suspend fun deleteUser(userId: Long)
}

class DefaultUserRepository @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override fun observeUser(userId: Long): Flow<User?> {
        return userDao.observeUserById(userId)
            .map { it?.toModel() }
    }

    override suspend fun saveUser(user: User) {
        userDao.saveUser(user.toEntity())
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user.toEntity())
    }

    override suspend fun updateUsername(
        userId: Long,
        username: String
    ) {
        userDao.updateUsername(userId, username)
    }

    override suspend fun updateLogin(
        userId: Long,
        login: String
    ) {
        userDao.updateUserLogin(userId, login)
    }

    override suspend fun updateAvatar(
        userId: Long,
        avatarUrl: String?
    ) {
        userDao.updateUserAvatar(userId, avatarUrl)
    }

    override suspend fun deleteUser(userId: Long) {
        val user = userDao.getUserById(userId)

        if (user != null) {
            userDao.deleteUser(user)
        }
    }
}
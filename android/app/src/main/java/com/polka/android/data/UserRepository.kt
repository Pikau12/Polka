package com.polka.android.data

import com.polka.android.data.database.dao.UserDao
import com.polka.android.data.model.User
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun observeCurrentUser(): Flow<User?>

    suspend fun saveUser(user: User)

    suspend fun updateUser(user: User)

    suspend fun updateUsername(username: String)

    suspend fun updateLogin(login: String)

    suspend fun updateAvatar(avatarUrl: String?)

    suspend fun deleteUser()
}

class DefaultUserRepository @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override fun observeCurrentUser(): Flow<User?> {
        TODO("Not implemented yet")
    }

    override suspend fun saveUser(user: User) {
        TODO("Not implemented yet")
    }

    override suspend fun updateUser(user: User) {
        TODO("Not implemented yet")
    }

    override suspend fun updateUsername(username: String) {
        TODO("Not implemented yet")
    }

    override suspend fun updateLogin(login: String) {
        TODO("Not implemented yet")
    }

    override suspend fun updateAvatar(avatarUrl: String?) {
        TODO("Not implemented yet")
    }

    override suspend fun deleteUser() {
        TODO("Not implemented yet")
    }
}
package com.polka.android.data

import com.polka.android.data.model.User
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

interface FriendRepository {
    fun getUserFriends(userId: Long): Flow<List<User>>
    suspend fun sendFriendRequest(userId: Long,friendId: Long)
    suspend fun acceptFriendRequest( requestId: Long)
    suspend fun rejectFriendRequest(requestId: Long)
    suspend fun removeFriend(userId: Long,friendId: Long)
}

class DefaultFriendRepository @Inject constructor() : FriendRepository {
    override fun getUserFriends(userId: Long): Flow<List<User>> {
        TODO("Not implemented yet")
    }

    override suspend fun sendFriendRequest(userId: Long, friendId: Long) {
        TODO("Not implemented yet")
    }

    override suspend fun acceptFriendRequest(requestId: Long) {
        TODO("Not implemented yet")
    }

    override suspend fun rejectFriendRequest(requestId: Long) {
        TODO("Not implemented yet")
    }

    override suspend fun removeFriend(userId: Long, friendId: Long) {
        TODO("Not implemented yet")
    }
}

package com.polka.android.data

import jakarta.inject.Inject

interface FriendRepository {
    fun getUserFriends(userId: Long): Flow<List<User>>
    suspend fun sendFriendRequest(userId: Long,friendId: Long)
    suspend fun acceptFriendRequest( requestId: Long)
    suspend fun rejectFriendRequest(requestId: Long)
    suspend fun removeFriend(userId: Long,friendId: Long)
}

class DefaultFriendRepository @Inject constructor() : FriendRepository {
}

package com.polka.android.data

import com.polka.android.data.database.dao.SearchDao
import com.polka.android.data.mapper.toModel
import com.polka.android.data.model.CollectionItem
import com.polka.android.data.model.Game
import com.polka.android.data.model.User
import com.polka.android.presentation.navigation.Destination
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchRepository @Inject constructor(
    val searchDao: SearchDao
) {
    fun searchUsersByUsername(name: String): Flow<List<User>> {
        return searchDao.searchUsersByUsername(name).map {entities -> entities.map{entity -> entity.toModel(entity)}}
    }


    /*
    TODO: image problem
     */
//    fun searchGameInCollection(gameName: String, userId: Long): Flow<List<Game>> {
//
//    }

    /*
    TODO: image problem
     */
//    fun getSortedUserCollection(userId: Long): Flow<List<CollectionItem>> {
//
//    }

    fun getUserFriend(userId: Long): Flow<List<User>> {
        return searchDao.getUserFriends(userId).map{entities -> entities.map{entity -> entity.toModel(entity)}}
    }
}

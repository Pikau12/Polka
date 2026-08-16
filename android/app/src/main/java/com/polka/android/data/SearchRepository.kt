package com.polka.android.data

import com.polka.android.data.database.dao.SearchDao
import com.polka.android.data.mapper.toModel
import com.polka.android.data.model.CollectionItem
import com.polka.android.data.model.Game
import com.polka.android.data.model.SortQuery
import com.polka.android.data.model.User
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface SearchRepository {
    fun searchUsersByUsername(name: String): Flow<List<User>>
    fun getUserFriends(userId: Long): Flow<List<User>>
    fun searchGamesInBgg(query: String): Flow<List<Game>>
    fun searchGamesByTagsInBgg(tags: List<String>): Flow<List<Game>>
    fun searchGamesInCollection(userId: Long, query: String): Flow<List<Game>>
    fun searchGamesInCollectionByTags(userId: Long, tags: List<String>): Flow<List<Game>>
    fun getSortedUserCollection(userId: Long, sortQuery: SortQuery): Flow<List<CollectionItem>>
}

class DefaultSearchRepository @Inject constructor(
    val searchDao: SearchDao
) : SearchRepository {
    override fun searchUsersByUsername(name: String): Flow<List<User>> {
        return searchDao.searchUsersByUsername(name)
            .map { entities -> entities.map { entity -> entity.toModel(entity) } }
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

    // TODO FriendDAO
    override fun getUserFriends(userId: Long): Flow<List<User>> {
        return searchDao.getUserFriends(userId)
            .map { entities -> entities.map { entity -> entity.toModel(entity) } }
    }

    override fun searchGamesInBgg(query: String): Flow<List<Game>> {
        TODO("Not implemented yet")
    }

    override fun searchGamesByTagsInBgg(tags: List<String>): Flow<List<Game>> {
        TODO("Not implemented yet")
    }

    override fun searchGamesInCollection(userId: Long, query: String): Flow<List<Game>> {
        TODO("Not implemented yet")
    }

    override fun searchGamesInCollectionByTags(userId: Long, tags: List<String>): Flow<List<Game>> {
        TODO("Not implemented yet")
    }

    override fun getSortedUserCollection(userId: Long, sortQuery: SortQuery): Flow<List<CollectionItem>> {
        TODO("Not implemented yet")
    }
}

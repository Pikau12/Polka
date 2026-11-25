package com.polka.android.data

import android.util.Log
import com.polka.android.data.database.dao.CollectionDao
import com.polka.android.data.model.CollectionItem
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

interface CollectionRepository {
    fun observeCollection(): Flow<List<CollectionItem>>
    /** Sets the rating (`null` clears it). Rating a game that is not in the collection is allowed. */
    suspend fun updateItemRating(item: CollectionItem.Id, rating: Int?)

    /** Clears the rating. If the game is not in the collection, its record is deleted. */
    suspend fun clearItemRating(item: CollectionItem.Id)
    suspend fun updateItemStatus(item: CollectionItem.Id, status: Set<CollectionItem.Status>)

    /**
     * Moves display order of the `itemToMove` right above the `aboveItem` or `null` if moved
     * at the very beginning and right below the `belowItem` or `null` if moved at the very end.
     * The resulting order in the list would be `[aboveItem, itemToMove, belowItem]`.
     *
     * If both `aboveItem` and `belowItem` are null, item will be dropped at the very end of the list.
     * If only one of the `aboveItem` or `belowItem` is null, the item will be dropped at the beginning
     * or the end of the list respectively, regardless of the other value.
     */
    suspend fun moveItemInBetween(
        itemToMove: CollectionItem.Id,
        aboveItem: CollectionItem.Id?,
        belowItem: CollectionItem.Id?
    )

    fun getUserCollection(userId: Long): Flow<List<CollectionItem>>
    fun getUserCollectionOrdered(userId: Long): Flow<List<CollectionItem>>

    /** Adds the game to the end of the current user's collection. Does nothing if it is already there. */
    suspend fun addGameToCollection(gameId: Long)

    /** Sets the note (a blank note clears it). Works for games that are not in the collection too. */
    suspend fun updateItemNote(item: CollectionItem.Id, note: String)

    /** Clears the note. If the game is not in the collection and has no rating, its record is deleted. */
    suspend fun clearItemNote(item: CollectionItem.Id)
    suspend fun removeGameFromCollection(item: CollectionItem.Id)
}

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultCollectionRepository @Inject constructor(
    private val collectionDao: CollectionDao,
    private val authRepository: AuthRepository
) : CollectionRepository {
    override fun observeCollection(): Flow<List<CollectionItem>> {
        // Follows the logged-in account: switching or leaving the account resubscribes.
        return authRepository.currentUserId
            .filterNotNull()
            .flatMapLatest { userId -> collectionDao.getInCollection(userId) }
            .map { list -> list.map { CollectionItem(it) } }
    }

    override suspend fun updateItemRating(item: CollectionItem.Id, rating: Int?) {
        if (rating == null) {
            clearItemRating(item)
        } else {
            collectionDao.setRating(item.ownerId, item.gameId, rating)
        }
    }

    override suspend fun clearItemRating(item: CollectionItem.Id) {
        collectionDao.clearRating(item.ownerId, item.gameId)
    }

    override suspend fun updateItemStatus(
        item: CollectionItem.Id,
        status: Set<CollectionItem.Status>
    ) {
        // Having a status means the game is in the collection.
        if (status.isNotEmpty()) {
            collectionDao.addToEnd(item.ownerId, item.gameId)
        }
        collectionDao.updateStatus(item.ownerId, item.gameId, status)
    }

    override suspend fun moveItemInBetween(
        itemToMove: CollectionItem.Id,
        aboveItem: CollectionItem.Id?,
        belowItem: CollectionItem.Id?
    ) {
        if (aboveItem != null &&
            belowItem != null &&
            (itemToMove.ownerId != aboveItem.ownerId || aboveItem.ownerId != belowItem.ownerId)
        ) {
            Log.e(
                "CollectionRepository::moveItemInBetween",
                "Supplied items with different ownerId: itemToMove: ${itemToMove.ownerId}, aboveItem: ${aboveItem.ownerId}, belowItem: ${belowItem.ownerId}"
            )
        }

        collectionDao.moveItemInBetween(
            itemToMove.ownerId,
            itemToMove.gameId,
            aboveItem?.gameId,
            belowItem?.gameId
        )
    }

    override suspend fun addGameToCollection(gameId: Long) {
        val user = authRepository.getCurrentUser()
        collectionDao.addToEnd(user.id, gameId)
    }

    override fun getUserCollection(userId: Long): Flow<List<CollectionItem>> {
        TODO("Not implemented yet")
    }

    override fun getUserCollectionOrdered(userId: Long): Flow<List<CollectionItem>> {
        TODO("Not implemented yet")
    }

    override suspend fun updateItemNote(item: CollectionItem.Id, note: String) {
        collectionDao.setNote(item.ownerId, item.gameId, note)
    }

    override suspend fun clearItemNote(item: CollectionItem.Id) {
        collectionDao.clearNote(item.ownerId, item.gameId)
    }

    override suspend fun removeGameFromCollection(item: CollectionItem.Id) {
        collectionDao.removeFromCollection(item.ownerId, item.gameId)
    }
}
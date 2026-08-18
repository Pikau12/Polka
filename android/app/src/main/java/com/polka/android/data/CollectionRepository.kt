package com.polka.android.data

import android.util.Log
import com.polka.android.data.database.dao.CollectionDao
import com.polka.android.data.model.CollectionItem
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CollectionRepository {
    fun observeCollection(): Flow<List<CollectionItem>>
    suspend fun updateItemRating(item: CollectionItem.Id, rating: Int?)
    suspend fun updateItemStatus(item: CollectionItem.Id, status: CollectionItem.Status)

    /**
     * Moves display order of the `itemToMove` right above the `aboveItem` or `null` if moved
     * at the very beginning and right below the `belowItem` or `null` if moved at the very end.
     * The resulting order in the list would be `[aboveItem, itemToMove, belowItem]`.
     *
     * If both `aboveItem` and `belowItem` are null, item will be dropped at the very end of the list.
     * If only one of the `aboveItem` or `belowItem` is null, the item will be dropped at the beginning
     * or the end of the list respectively, regardless of the other value.
     */
    suspend fun moveItemInBetween(itemToMove: CollectionItem.Id, aboveItem: CollectionItem.Id?, belowItem: CollectionItem.Id?)

    fun getUserCollection(userId: Long): Flow<List<CollectionItem>>
    fun getUserCollectionOrdered(userId: Long): Flow<List<CollectionItem>>

    //TODO games methods

    suspend fun addNote(item: CollectionItem.Id,note: String)
    suspend fun updateNote(item: CollectionItem.Id,note: String)
    suspend fun deleteNote(item: CollectionItem.Id)
    suspend fun removeGameFromCollection(item: CollectionItem.Id)
}

class DefaultCollectionRepository @Inject constructor(private val collectionDao: CollectionDao) : CollectionRepository {
    override fun observeCollection(): Flow<List<CollectionItem>> {
        return collectionDao.getAll().map { list ->
            list.map { CollectionItem(it) }
        }
    }

    override suspend fun updateItemRating(item: CollectionItem.Id, rating: Int?) {
        collectionDao.updateRating(item.ownerId, item.gameId, rating)
    }

    override suspend fun updateItemStatus(item: CollectionItem.Id, status: CollectionItem.Status) {
        collectionDao.updateStatus(item.ownerId, item.gameId, status)
    }

    override suspend fun moveItemInBetween(itemToMove: CollectionItem.Id, aboveItem: CollectionItem.Id?, belowItem: CollectionItem.Id?) {
        if (aboveItem != null &&
            belowItem != null &&
            (itemToMove.ownerId != aboveItem.ownerId || aboveItem.ownerId != belowItem.ownerId)) {
            Log.e("CollectionRepository::moveItemInBetween", "Supplied items with different ownerId: itemToMove: ${itemToMove.ownerId}, aboveItem: ${aboveItem.ownerId}, belowItem: ${belowItem.ownerId}")
        }

        collectionDao.moveItemInBetween(itemToMove.ownerId, itemToMove.gameId, aboveItem?.gameId, belowItem?.gameId)
    }

    override fun getUserCollection(userId: Long): Flow<List<CollectionItem>> {
        TODO("Not implemented yet")
    }

    override fun getUserCollectionOrdered(userId: Long): Flow<List<CollectionItem>> {
        TODO("Not implemented yet")
    }

    override suspend fun addNote(item: CollectionItem.Id, note: String) {
        TODO("Not implemented yet")
    }

    override suspend fun updateNote(item: CollectionItem.Id, note: String) {
        TODO("Not implemented yet")
    }

    override suspend fun deleteNote(item: CollectionItem.Id) {
        TODO("Not implemented yet")
    }

    override suspend fun removeGameFromCollection(item: CollectionItem.Id) {
        TODO("Not implemented yet")
    }
}

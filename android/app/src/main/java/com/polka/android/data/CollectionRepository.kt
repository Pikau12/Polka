package com.polka.android.data

import android.util.Log
import com.polka.android.data.database.dao.CollectionDao
import com.polka.android.data.model.CollectionItem
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CollectionRepository @Inject constructor(private val collectionDao: CollectionDao) {
    fun observeCollection(): Flow<List<CollectionItem>> {
        return collectionDao.getAll().map { list ->
            list.map { CollectionItem(it) }
        }
    }

    suspend fun updateItemRating(item: CollectionItem.Id, rating: Int?) {
        collectionDao.updateRating(item.ownerId, item.gameId, rating)
    }

    suspend fun updateItemStatus(item: CollectionItem.Id, status: CollectionItem.Status) {
        collectionDao.updateStatus(item.ownerId, item.gameId, status)
    }

    /**
     * Moves display order of the `itemToMove` right above the `aboveItem` or `null` if moved
     * at the very beginning and right below the `belowItem` or `null` if moved at the very end.
     * The resulting order in the list would be `[aboveItem, itemToMove, belowItem]`.
     *
     * If both `aboveItem` and `belowItem` are null, item will be dropped at the very end of the list.
     * If only one of the `aboveItem` or `belowItem` is null, the item will be dropped at the beginning
     * or the end of the list respectively, regardless of the other value.
     */
    suspend fun moveItemInBetween(itemToMove: CollectionItem.Id, aboveItem: CollectionItem.Id?, belowItem: CollectionItem.Id?) {
        if (aboveItem != null &&
            belowItem != null &&
            (itemToMove.ownerId != aboveItem.ownerId || aboveItem.ownerId != belowItem.ownerId)) {
            Log.e("CollectionRepository::moveItemInBetween", "Supplied items with different ownerId: itemToMove: ${itemToMove.ownerId}, aboveItem: ${aboveItem.ownerId}, belowItem: ${belowItem.ownerId}")
        }

        collectionDao.moveItemInBetween(itemToMove.ownerId, itemToMove.gameId, aboveItem?.gameId, belowItem?.gameId)
    }
}

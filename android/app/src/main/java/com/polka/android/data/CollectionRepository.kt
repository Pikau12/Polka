package com.polka.android.data

import com.polka.android.data.database.dao.CollectionDao
import com.polka.android.data.model.CollectionItem
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CollectionRepository @Inject constructor(private val collectionDao: CollectionDao) {
    fun observeCollection(): Flow<Map<Long, CollectionItem>> {
        return collectionDao.getAll().map { list ->
            list.associate {it.gameId to CollectionItem(it)}
        }
    }

    suspend fun updateItemRating(item: Long, rating: Int?) {
        collectionDao.updateRating(item, rating)
    }

    suspend fun updateItemStatus(item: Long, status: CollectionItem.Status) {
        collectionDao.updateStatus(item, status)
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
    suspend fun moveItemInBetween(itemToMove: Long, aboveItem: Long?, belowItem: Long?) {
        collectionDao.moveItemInBetween(itemToMove, aboveItem, belowItem)
    }
}

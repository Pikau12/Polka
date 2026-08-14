package com.polka.android.data.database.dao

import android.util.Log
import androidx.room.Query
import androidx.room.Transaction
import com.polka.android.data.database.model.CollectionItemEntity
import com.polka.android.data.model.CollectionItem
import kotlinx.coroutines.flow.Flow

interface CollectionDao {
    @Query("SELECT * FROM collection_items")
    fun getAll(): Flow<List<CollectionItemEntity>>

    @Query("UPDATE collection_items SET rating = :rating WHERE gameId = :id")
    suspend fun updateRating(id: Long, rating: Int?)

    @Query("UPDATE collection_items SET status = :status WHERE gameId = :id")
    suspend fun updateStatus(id: Long, status: CollectionItem.Status)

    @Query("UPDATE collection_items SET displayOrder = :newOrder WHERE gameId = :id")
    suspend fun updateOrder(id: Long, newOrder: Double)

    @Query("SELECT MIN(displayOrder) FROM collection_items")
    suspend fun getMinOrder(): Double?

    @Query("SELECT MAX(displayOrder) FROM collection_items")
    suspend fun getMaxOrder(): Double?

    @Query("SELECT displayOrder FROM collection_items WHERE gameId = :id")
    suspend fun getOrderById(id: Long): Double?

    /**
     * @see com.polka.android.data.CollectionRepository.moveItemInBetween
     */
    @Transaction
    suspend fun moveItemInBetween(itemToMove: Long, aboveItem: Long?, belowItem: Long?) {
        val tag = "CollectionDao::moveItemInBetween"

        val newOrder = when {
            // Dropped between two items
            aboveItem != null && belowItem != null -> {
                val orderAbove = getOrderById(aboveItem) ?: return Log.e(
                    tag, "`aboveItem` didn't correspond to a valid id"
                ).let {}
                val orderBelow = getOrderById(belowItem) ?: return Log.e(
                    tag, "`belowItem` didn't correspond to a valid id"
                ).let {}
                (orderAbove + orderBelow) / 2.0
            }

            // Dropped at the very end
            belowItem == null -> {
                val currentMax = getMaxOrder() ?: 1.0
                currentMax + 1.0
            }

            // Dropped at the very beginning
            else -> {
                val currentMin = getMinOrder() ?: 1.0
                currentMin - 1.0
            }
        }

        updateOrder(itemToMove, newOrder)
    }
}

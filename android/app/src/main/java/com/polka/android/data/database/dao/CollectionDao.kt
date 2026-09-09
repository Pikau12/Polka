package com.polka.android.data.database.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.polka.android.data.database.AppDatabase.Companion.NOW_MS
import com.polka.android.data.database.model.CollectionItemEntity
import com.polka.android.data.model.CollectionItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collection_items WHERE ownerId = :ownerId")
    fun getAll(ownerId: Long): Flow<List<CollectionItemEntity>>

    @Query("UPDATE collection_items SET rating = :rating, updatedAt = $NOW_MS WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun updateRating(ownerId: Long, gameId: Long, rating: Int?)

    @Query("UPDATE collection_items SET status = :status, updatedAt = $NOW_MS WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun updateStatus(ownerId: Long, gameId: Long, status: Set<CollectionItem.Status>)

    @Query("UPDATE collection_items SET displayOrder = :newOrder, updatedAt = $NOW_MS WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun updateOrder(ownerId: Long, gameId: Long, newOrder: Double)

    @Query("SELECT MIN(displayOrder) FROM collection_items WHERE ownerId = :ownerId")
    suspend fun getMinOrder(ownerId: Long): Double?

    @Query("SELECT MAX(displayOrder) FROM collection_items WHERE ownerId = :ownerId")
    suspend fun getMaxOrder(ownerId: Long): Double?

    @Query("SELECT displayOrder FROM collection_items WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun getOrderById(ownerId: Long, gameId: Long): Double?

    @Query("SELECT gameId FROM collection_items WHERE ownerId = :ownerId ORDER BY displayOrder ASC")
    suspend fun getOrderedIds(ownerId: Long): List<Long>

    /**
     * @see com.polka.android.data.CollectionRepository.moveItemInBetween
     */
    @Transaction
    suspend fun moveItemInBetween(
        ownerId: Long,
        itemToMove: Long,
        aboveItem: Long?,
        belowItem: Long?
    ) {
        val epsilon = 1e-12
        val tag = "CollectionDao::moveItemInBetween"

        val newOrder = when {
            // Dropped between two items
            aboveItem != null && belowItem != null -> {
                val orderAbove = getOrderById(ownerId, aboveItem) ?: return Log.e(
                    tag, "`aboveItem` didn't correspond to a valid id"
                ).let {}
                val orderBelow = getOrderById(ownerId, belowItem) ?: return Log.e(
                    tag, "`belowItem` didn't correspond to a valid id"
                ).let {}
                if (orderAbove - orderBelow < epsilon) {
                    null
                } else {
                    (orderAbove + orderBelow) / 2.0
                }
            }

            // Dropped at the very end
            belowItem == null -> {
                val currentMax = getMaxOrder(ownerId) ?: 1.0
                currentMax + 1.0
            }

            // Dropped at the very beginning
            else -> {
                val currentMin = getMinOrder(ownerId) ?: 1.0
                currentMin - 1.0
            }
        }

        if (newOrder == null) { // distance between orders is too small, updating all orders
            val ordered = getOrderedIds(ownerId)
            var currentOrder = 1.0
            for (id in ordered) {
                if (id == itemToMove) {
                    continue
                }
                updateOrder(ownerId, id, currentOrder)
                currentOrder += 1.0
                if (id == aboveItem) {
                    updateOrder(ownerId, itemToMove, currentOrder)
                    currentOrder += 1.0
                }
            }
        } else {
            updateOrder(ownerId, itemToMove, newOrder)
        }
    }
}

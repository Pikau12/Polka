package com.polka.android.data.database.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.polka.android.data.database.model.CollectionItemEntity
import com.polka.android.data.model.CollectionItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collection_items")
    fun getAll(): Flow<List<CollectionItemEntity>>

    @Query("UPDATE collection_items SET rating = :rating WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun updateRating(ownerId: Long, gameId: Long, rating: Int?)

    @Query("UPDATE collection_items SET status = :status WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun updateStatus(ownerId: Long, gameId: Long, status: Set<CollectionItem.Status>)

    @Query("UPDATE collection_items SET displayOrder = :newOrder WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun updateOrder(ownerId: Long, gameId: Long, newOrder: Double)

    @Query("SELECT MIN(displayOrder) FROM collection_items WHERE ownerId = :ownerId")
    suspend fun getMinOrder(ownerId: Long): Double?

    @Query("SELECT MAX(displayOrder) FROM collection_items WHERE ownerId = :ownerId")
    suspend fun getMaxOrder(ownerId: Long): Double?

    @Query("SELECT displayOrder FROM collection_items WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun getOrderById(ownerId: Long, gameId: Long): Double?

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
                (orderAbove + orderBelow) / 2.0
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

        updateOrder(ownerId, itemToMove, newOrder)
    }
}

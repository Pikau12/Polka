package com.polka.android.data.database.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.polka.android.data.database.AppDatabase.Companion.NOW_MS
import com.polka.android.data.database.model.CollectionItemEntity
import com.polka.android.data.model.CollectionItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    /** All records of the user, including rated games that are not in the collection. */
    @Query("SELECT * FROM collection_items WHERE ownerId = :ownerId")
    fun getAll(ownerId: Long): Flow<List<CollectionItemEntity>>

    /** Only the games that are in the user's collection. */
    @Query("SELECT * FROM collection_items WHERE ownerId = :ownerId AND isInCollection = 1")
    fun getInCollection(ownerId: Long): Flow<List<CollectionItemEntity>>

    @Query("SELECT * FROM collection_items WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun getItem(ownerId: Long, gameId: Long): CollectionItemEntity?

    @Query("DELETE FROM collection_items WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun delete(ownerId: Long, gameId: Long)

    @Query("UPDATE collection_items SET rating = :rating, updatedAt = $NOW_MS WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun updateRating(ownerId: Long, gameId: Long, rating: Int?): Int

    @Query("UPDATE collection_items SET status = :status, updatedAt = $NOW_MS WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun updateStatus(ownerId: Long, gameId: Long, status: Set<CollectionItem.Status>)

    @Query("UPDATE collection_items SET displayOrder = :newOrder, updatedAt = $NOW_MS WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun updateOrder(ownerId: Long, gameId: Long, newOrder: Double)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: CollectionItemEntity)

    @Query("UPDATE collection_items SET isInCollection = :value, updatedAt = $NOW_MS WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun setInCollection(ownerId: Long, gameId: Long, value: Boolean)

    @Query("UPDATE collection_items SET isInCollection = 1, displayOrder = :order, updatedAt = $NOW_MS WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun putInCollectionAt(ownerId: Long, gameId: Long, order: Double)

    /**
     * Adds the game to the end of the owner's collection.
     * - no record yet: creates it;
     * - the record exists only because of a rating: returns it to the collection;
     * - already in the collection: does nothing.
     */
    @Transaction
    suspend fun addToEnd(ownerId: Long, gameId: Long) {
        val existing = getItem(ownerId, gameId)
        if (existing != null && existing.isInCollection) return

        val order = (getMaxOrder(ownerId) ?: 0.0) + 1.0
        if (existing == null) {
            insert(
                CollectionItemEntity(
                    ownerId = ownerId,
                    gameId = gameId,
                    displayOrder = order,
                    note = "",
                    rating = null,
                    status = emptySet(),
                    images = emptyList(),
                    isInCollection = true,
                )
            )
        } else {
            putInCollectionAt(ownerId, gameId, order)
        }
    }

    /**
     * Sets the rating. If the game has no record yet (it is not in the collection),
     * creates a record that is hidden from the collection list.
     */
    @Transaction
    suspend fun setRating(ownerId: Long, gameId: Long, rating: Int) {
        val updated = updateRating(ownerId, gameId, rating)
        if (updated == 0) {
            insert(
                CollectionItemEntity(
                    ownerId = ownerId,
                    gameId = gameId,
                    displayOrder = 0.0,
                    note = "",
                    rating = rating,
                    status = emptySet(),
                    images = emptyList(),
                    isInCollection = false,
                )
            )
        }
    }

    /**
     * Clears the rating. A record of a game outside the collection that holds nothing else
     * is deleted.
     */
    @Transaction
    suspend fun clearRating(ownerId: Long, gameId: Long) {
        val item = getItem(ownerId, gameId) ?: return
        if (!item.isInCollection && item.note.isEmpty() && item.images.isEmpty()) {
            delete(ownerId, gameId)
        } else {
            updateRating(ownerId, gameId, null)
        }
    }

    /**
     * Removes the game from the collection and resets its statuses. If the record still holds
     * the user's data (rating, note, images), it stays hidden; otherwise it is deleted.
     */
    @Transaction
    suspend fun removeFromCollection(ownerId: Long, gameId: Long) {
        val item = getItem(ownerId, gameId) ?: return
        if (item.rating == null && item.note.isEmpty() && item.images.isEmpty()) {
            delete(ownerId, gameId)
        } else {
            updateStatus(ownerId, gameId, emptySet())
            setInCollection(ownerId, gameId, false)
        }
    }

    @Query("UPDATE collection_items SET note = :note, updatedAt = $NOW_MS WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun updateNote(ownerId: Long, gameId: Long, note: String): Int

    /**
     * Sets the note (a blank note clears it). If the game has no record yet
     * (it is not in the collection), creates a record that is hidden from the collection list.
     */
    @Transaction
    suspend fun setNote(ownerId: Long, gameId: Long, note: String) {
        if (note.isBlank()) {
            clearNote(ownerId, gameId)
            return
        }
        val updated = updateNote(ownerId, gameId, note)
        if (updated == 0) {
            insert(
                CollectionItemEntity(
                    ownerId = ownerId,
                    gameId = gameId,
                    displayOrder = 0.0,
                    note = note,
                    rating = null,
                    status = emptySet(),
                    images = emptyList(),
                    isInCollection = false,
                )
            )
        }
    }

    /**
     * Clears the note. A record of a game outside the collection that holds nothing else
     * is deleted.
     */
    @Transaction
    suspend fun clearNote(ownerId: Long, gameId: Long) {
        val item = getItem(ownerId, gameId) ?: return
        if (!item.isInCollection && item.rating == null && item.images.isEmpty()) {
            delete(ownerId, gameId)
        } else {
            updateNote(ownerId, gameId, "")
        }
    }

    @Query("SELECT MIN(displayOrder) FROM collection_items WHERE ownerId = :ownerId AND isInCollection = 1")
    suspend fun getMinOrder(ownerId: Long): Double?

    @Query("SELECT MAX(displayOrder) FROM collection_items WHERE ownerId = :ownerId AND isInCollection = 1")
    suspend fun getMaxOrder(ownerId: Long): Double?

    @Query("SELECT displayOrder FROM collection_items WHERE ownerId = :ownerId AND gameId = :gameId")
    suspend fun getOrderById(ownerId: Long, gameId: Long): Double?

    @Query("SELECT gameId FROM collection_items WHERE ownerId = :ownerId AND isInCollection = 1 ORDER BY displayOrder ASC")
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
                if (orderBelow - orderAbove < epsilon) {
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
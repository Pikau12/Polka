package com.polka.android.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.polka.android.data.database.AppDatabase.Companion.NOW_MS
import com.polka.android.data.image.ImageSource
import com.polka.android.data.model.CollectionItem

@Entity(
    tableName = "collection_items",
    primaryKeys = ["ownerId", "gameId"],

    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = ["id"],
            childColumns = ["gameId"],
            onDelete = ForeignKey.CASCADE
        )
    ],

    indices = [
        Index("ownerId"),
        Index("gameId"),
    ],
)
data class CollectionItemEntity(
    val ownerId: Long,
    val gameId: Long,
    /**
     * Fractional, so that on order update, we can quickly move it to `(aboveId.displayOrder + belowId.displayOrder)/2`
     */
    val displayOrder: Double,

    val note: String,
    val rating: Int?,
    val status: Set<CollectionItem.Status>,
    val images: List<ImageSource.Saved>,

    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(defaultValue = NOW_MS)
    val updatedAt: Long = System.currentTimeMillis(),
)
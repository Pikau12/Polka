package com.polka.android.data.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.polka.android.data.image.ImageSource
import com.polka.android.data.model.CollectionItem

@Entity(
    tableName = "collection_items",
    foreignKeys = [ForeignKey(
        entity = GameEntity::class,
        parentColumns = ["id"],
        childColumns = ["gameId"],
        onDelete = ForeignKey.CASCADE
    )],
)
data class CollectionItemEntity(
    @PrimaryKey val gameId: Long,
    /**
     * Fractional, so that on order update, we can quickly move it to `(aboveId.displayOrder + belowId.displayOrder)/2`
     */
    val displayOrder: Double,

    val note: String,
    val rating: Int?,
    val status: CollectionItem.Status,
    val images: List<ImageSource.Saved>,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)
package com.polka.android.data.model

import com.polka.android.data.database.model.CollectionItemEntity
import com.polka.android.data.image.ImageSource
import com.polka.android.data.model.CollectionItem.Status

data class CollectionItem(
    val gameId: Long,
    val displayOrder: Double,
    val note: String,
    val rating: Int?,
    val status: Status,
    val images: List<ImageSource>,
) {
    enum class Status {
        NO_STATUS, MUST_HAVE, LOVE_TO_HAVE, LIKE_TO_HAVE, THINKING, DO_NOT_BUY;

        override fun toString(): String {
            return when (this) {
                NO_STATUS -> "-"
                MUST_HAVE -> "Must have"
                LOVE_TO_HAVE -> "Love to have"
                LIKE_TO_HAVE -> "Like to have"
                THINKING -> "Thinking about it"
                DO_NOT_BUY -> "Don't buy this"
            }
        }
    }

    constructor(entity: CollectionItemEntity) : this(
        entity.gameId, entity.displayOrder, entity.note, entity.rating, entity.status, entity.images
    )
}

fun String.toCollectionItemStatus(): Status? {
    return when (this) {
        "-" -> Status.NO_STATUS
        "Must have" -> Status.MUST_HAVE
        "Love to have" -> Status.LOVE_TO_HAVE
        "Like to have" -> Status.LIKE_TO_HAVE
        "Thinking about it" -> Status.THINKING
        "Don't buy this" -> Status.DO_NOT_BUY
        else -> null
    }
}
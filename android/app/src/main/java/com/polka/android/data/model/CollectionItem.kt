package com.polka.android.data.model

import com.polka.android.data.database.model.CollectionItemEntity
import com.polka.android.data.image.ImageSource
import java.util.EnumSet

data class CollectionItem(
    val gameId: Long,
    val displayOrder: Double,
    val note: String,
    val rating: Int?,
    val status: EnumSet<Status>,
    val images: List<ImageSource>,
) {
    enum class Status {
        OWN, PREVIOUSLY_OWNED, FOR_TRADE, WANT_IN_TRADE, WANT_TO_PLAY, WANT_TO_BUY, PREORDERED,
        WISHLIST_MUST_HAVE, WISHLIST_LOVE_TO_HAVE, WISHLIST_LIKE_TO_HAVE, WISHLIST_THINKING, WISHLIST_DO_NOT_BUY;

        enum class Wishlist {
            MUST_HAVE, LOVE_TO_HAVE, LIKE_TO_HAVE, THINKING, DO_NOT_BUY;

            fun toStatus(): Status {
                return when (this) {
                    MUST_HAVE -> WISHLIST_MUST_HAVE
                    LOVE_TO_HAVE -> WISHLIST_LOVE_TO_HAVE
                    LIKE_TO_HAVE -> WISHLIST_LIKE_TO_HAVE
                    THINKING -> WISHLIST_THINKING
                    DO_NOT_BUY -> WISHLIST_DO_NOT_BUY
                }
            }

            override fun toString(): String {
                return when (this) {
                    MUST_HAVE -> "Must have"
                    LOVE_TO_HAVE -> "Love to have"
                    LIKE_TO_HAVE -> "Like to have"
                    THINKING -> "Thinking about it"
                    DO_NOT_BUY -> "Don't buy this"
                }
            }
        }

        fun toWishlist(): Wishlist? {
            return when (this) {
                WISHLIST_MUST_HAVE -> Wishlist.MUST_HAVE
                WISHLIST_LOVE_TO_HAVE -> Wishlist.LOVE_TO_HAVE
                WISHLIST_LIKE_TO_HAVE -> Wishlist.LIKE_TO_HAVE
                WISHLIST_THINKING -> Wishlist.THINKING
                WISHLIST_DO_NOT_BUY -> Wishlist.DO_NOT_BUY
                else -> null
            }
        }

        override fun toString(): String {
            return when (this) {
                OWN -> "Own"
                PREVIOUSLY_OWNED -> "Previously Owned"
                FOR_TRADE -> "For Trade"
                WANT_IN_TRADE -> "Want in Trade"
                WANT_TO_PLAY -> "Want to Play"
                WANT_TO_BUY -> "Want to Buy"
                PREORDERED -> "Preordered"
                else -> "Wishlist"
            }
        }

        fun EnumSet<Status>.wishlist(wishlist: Wishlist) {
            removeAll(Wishlist.entries.map { it.toStatus() })
            add(wishlist.toStatus())
        }
    }

    constructor(entity: CollectionItemEntity) : this(
        entity.gameId, entity.displayOrder, entity.note, entity.rating, entity.status, entity.images
    )
}

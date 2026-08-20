package com.polka.android.presentation.model

import coil3.request.ImageRequest
import com.polka.android.data.model.CollectionItem
import java.util.EnumSet

/**
 * Class that represents [CollectionItem] from data/model to UI layer
 * with more appropriate structure
 */
data class CollectionItem (
    var name: String,
    var id: Id,
    var image: ImageRequest? = null,
    var status: EnumSet<Status>,
    var rating: Int? = null,
){
    fun getMostPopularOfExistStatus() : Status {
        if (Status.OWN in status){
            return Status.OWN
        }
        else if (Status.PREVIOUSLY_OWNED in status){
            return Status.PREVIOUSLY_OWNED
        }
        else if (Status.FOR_TRADE in status) {
            return Status.FOR_TRADE
        }
        else if (Status.WANT_IN_TRADE in status) {
            return Status.WANT_IN_TRADE
        }
        else if (Status.WANT_TO_PLAY in status) {
            return Status.WANT_TO_PLAY
        }
        else if (Status.WANT_TO_BUY in status) {
            return Status.WANT_TO_BUY
        }
        else if (Status.PREORDERED in status) {
            return Status.PREORDERED
        }
        //
        else if (Status.WISHLIST_MUST_HAVE in status) {
            return Status.WISHLIST_MUST_HAVE
        }
        else if (Status.WISHLIST_LOVE_TO_HAVE in status) {
            return Status.WISHLIST_LOVE_TO_HAVE
        }
        else if (Status.WISHLIST_LIKE_TO_HAVE in status) {
            return Status.WISHLIST_LIKE_TO_HAVE
        }
        else if (Status.WISHLIST_THINKING in status) {
            return Status.WISHLIST_THINKING
        }
        else {
            return Status.WISHLIST_DO_NOT_BUY
        }
    }
    data class Id(val ownerId: Long, val gameId: Long)

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

        fun toDataStatus(): CollectionItem.Status {
            return CollectionItem.Status.valueOf(this.name)
        }
    }
}

fun CollectionItem.Status.toUIStatus(): com.polka.android.presentation.model.CollectionItem.Status {
    return com.polka.android.presentation.model.CollectionItem.Status.valueOf(this.name)
}

fun EnumSet<CollectionItem.Status>.toUIStatusSet(): EnumSet<com.polka.android.presentation.model.CollectionItem.Status> {
    val result = EnumSet.noneOf(com.polka.android.presentation.model.CollectionItem.Status::class.java)
    this.forEach { result.add(it.toUIStatus()) }
    return result
}

fun EnumSet<com.polka.android.presentation.model.CollectionItem.Status>.toDataStatusSet(): EnumSet<CollectionItem.Status> {
    val result = EnumSet.noneOf(CollectionItem::class.java)
    this.forEach { result.add(it.toDataStatus()) }
    return result
}
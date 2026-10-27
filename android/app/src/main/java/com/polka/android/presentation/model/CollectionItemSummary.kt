package com.polka.android.presentation.model

import coil3.request.ImageRequest

data class CollectionItemSummary (
    var id: Id,
    var name: String,
    var image: ImageRequest? = null,
    var releaseYear: String? = null,
    var communityRating: String? = null,
    var userRating: String? = null,
    var designer: String? = null,
) {
    data class Id(val ownerId: Long, val gameId: Long)
}
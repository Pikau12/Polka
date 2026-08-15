package com.polka.android.presentation.model

import coil3.request.ImageRequest

/**
 * Class that represents [CollectionItem] from data/model to UI layer
 * with more appropriate structure
 */
data class CollectionItem (
    var name: String,
    var ownerId: Long,
    var gameId: Long,
    var image: ImageRequest? = null,
    var status: String,
    var rating: Int? = null,
)
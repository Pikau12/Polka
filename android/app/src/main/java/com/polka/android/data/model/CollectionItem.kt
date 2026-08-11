package com.polka.android.data.model

import com.polka.android.data.image.ImageSource

data class CollectionItem(
    val gameId: Long,
    val status: String,
    val rating: Int?,
    val images: List<ImageSource>,
    val notes: String,
    val displayOrder: Int,
)

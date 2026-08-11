package com.polka.android.data.model

import com.polka.android.data.image.ImageSource

data class CollectionItem(
    val gameId: Long,
    val rating: Int? = null,
    val images: List<ImageSource> = listOf(),
    val notes: String = "",
    val displayOrder: Int,
)

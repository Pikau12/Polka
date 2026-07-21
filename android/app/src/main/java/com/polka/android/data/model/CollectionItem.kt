package com.polka.android.data.model

import coil3.Image
import com.polka.android.data.GameRepository

data class CollectionItem(
    val gameId: Long,
    val rating: Int?,
    val images: List<Image>,
    val notes: String,
    val displayOrder: Int,
)

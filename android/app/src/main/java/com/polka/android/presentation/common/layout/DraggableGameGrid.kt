package com.polka.android.presentation.common.layout

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import com.polka.android.data.model.CollectionItem

@Composable
fun DraggableGameGrid(
    collection: List<CollectionItem>,

){

    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
    ) {
        items(collection) { collectionItem ->

        }
    }
}
package com.polka.android.presentation.common.layout

import androidx.compose.runtime.Composable
import com.polka.android.presentation.model.CollectionItem
import com.polka.android.presentation.common.tiles.ContextMenuAction

@Composable
fun DraggableGameGrid(
    collection: List<CollectionItem>,
    onDoubleClick: (Long) -> Unit,
    onContextMenu: (Long, ContextMenuAction) -> Unit,
    onAddGameClick: () -> Unit,
){

}
package com.polka.android.presentation.common.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polka.android.presentation.common.tiles.AddGameTile
import com.polka.android.presentation.common.tiles.ContextMenuAction
import com.polka.android.presentation.common.tiles.GameTile
import com.polka.android.presentation.model.CollectionItem
import com.polka.android.presentation.theme.PolkaTheme
import okhttp3.Address
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyGridState

@Composable
fun VerticalReorderGrid(
    collection: List<CollectionItem>,
    onDoubleClick: (Long) -> Unit,
    onContextMenu: (Long, ContextMenuAction) -> Unit,
    onAddGameClick: () -> Unit,
) {
    var isDragging by remember { mutableStateOf(false) }

    val data = remember { mutableStateOf(collection) }
    val lazyGridState = rememberLazyGridState()
    val state = rememberReorderableLazyGridState(
        lazyGridState = lazyGridState,
        onMove = { from, to ->
            data.value = data.value.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        }
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        state = lazyGridState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // TODO: change color
        contentPadding = PaddingValues(16.dp, 16.dp),
        horizontalArrangement = Arrangement.spacedBy(13.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp),
        userScrollEnabled = !isDragging,
    ) {
        items(data.value, key = { it.collectionItemId }) {
            ReorderableItem(state, key = it.collectionItemId) { dragging ->
                LaunchedEffect(dragging) {
                    isDragging = dragging
                }

                GameTile(
                    collectionItem = it,
                    modifier = Modifier
                        .animateItem()
                        .then(
                            if (dragging)
                                Modifier.alpha(0.7f) else
                                Modifier
                        )
                        .longPressDraggableHandle(),
                    onDoubleClick = onDoubleClick,
                    onContextMenu = onContextMenu,
                )
            }
        } // TODO: add TileAddGame

        item{
            AddGameTile(
                onClick = onAddGameClick
            )
        }
    }
}

@Preview(
    name = "Grid for tiles of games",
    showBackground = true,
    backgroundColor = 0xFFF5F5F5,
    device = Devices.PIXEL_4
)
@Composable
fun ReorderableGridPreview(){
    val testGames = listOf(
        CollectionItem(
            name = "Catan",
            gameId = 1,
            collectionItemId = 1,
            displayOrder = 1,
            status = "Own",
            rating = 8
        ),
        CollectionItem(
            name = "Code Names",
            gameId = 1,
            collectionItemId = 2,
            displayOrder = 2,
            status = "Wishlist",
            rating = null
        ),
        CollectionItem(
            name = "Ticket to Ride",
            gameId = 1,
            collectionItemId = 3,
            displayOrder = 3,
            status = "Previous owned",
            rating = 7
        ),
        CollectionItem(
            name = "Azul",
            gameId = 1,
            collectionItemId = 4,
            displayOrder = 1,
            status = "Own",
            rating = 8
        ),
        CollectionItem(
            name = "Coffee",
            gameId = 1,
            collectionItemId = 5,
            displayOrder = 2,
            status = "Wishlist",
            rating = null
        ),
        CollectionItem(
            name = "Terraforming Mars",
            gameId = 1,
            collectionItemId = 6,
            displayOrder = 3,
            status = "Previous owned",
            rating = 7
        ),
    )

    PolkaTheme{
        VerticalReorderGrid(
            collection = testGames,
            onDoubleClick = { },
            onContextMenu = { _, _ -> },
            onAddGameClick = { }
        )
    }
}
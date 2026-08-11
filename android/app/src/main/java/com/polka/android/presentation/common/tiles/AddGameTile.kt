package com.polka.android.presentation.common.tiles

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polka.android.presentation.model.CollectionItem
import com.polka.android.presentation.theme.PolkaTheme

@Composable
fun AddGameTile(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(59/73f)
            .combinedClickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // TODO: change color
        )
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add game tile",
                    modifier = Modifier.size(55.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add game", // TODO: change string to string from values.xml
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(
    name = "GameTile Row (3 items)",
    showBackground = true,
    backgroundColor = 0xFFF5F5F5,
    device = Devices.PIXEL_4
)
@Composable
fun PreviewGameTileRowWithAddGameTile() {
    PolkaTheme {
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
                collectionItemId = 1,
                displayOrder = 2,
                status = "Wishlist",
                rating = null
            ),
            CollectionItem(
                name = "Ticket to Ride",
                gameId = 1,
                collectionItemId = 1,
                displayOrder = 3,
                status = "Previous owned",
                rating = 7
            )
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                testGames.forEach { collectionItem ->
                    GameTile(
                        collectionItem = collectionItem,
                        modifier = Modifier.weight(1f),
                        onDoubleClick = { /* заглушка */ },
                        onContextMenu = { _, _ -> /* заглушка */ },
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                testGames.forEach { collectionItem ->
                    GameTile(
                        collectionItem = collectionItem,
                        modifier = Modifier.weight(1f),
                        onDoubleClick = { /* заглушка */ },
                        onContextMenu = { _, _ -> /* заглушка */ },
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                GameTile(
                    collectionItem = testGames[0],
                    modifier = Modifier.weight(1f),
                    onDoubleClick = { /* заглушка */ },
                    onContextMenu = { _, _ -> /* заглушка */ },
                )

                GameTile(
                    collectionItem = testGames[1],
                    modifier = Modifier.weight(1f),
                    onDoubleClick = { /* заглушка */ },
                    onContextMenu = { _, _ -> /* заглушка */ },
                )

                AddGameTile(
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
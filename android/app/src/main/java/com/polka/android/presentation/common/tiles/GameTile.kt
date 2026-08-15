package com.polka.android.presentation.common.tiles

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.polka.android.R
import com.polka.android.presentation.theme.PolkaAcceptButtonColors
import com.polka.android.presentation.theme.PolkaTheme
import com.polka.android.utils.AdaptiveTextForGameTile
import com.polka.android.presentation.model.CollectionItem

sealed class ContextMenuAction {
    object onStatusClick : ContextMenuAction()
    object onRatingClick : ContextMenuAction()
    object onAddSessionClick : ContextMenuAction()
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun GameTile(
    collectionItem: CollectionItem,
    modifier: Modifier = Modifier,
    onDoubleClick: (Long) -> Unit,
    onContextMenu: (CollectionItem.Id, ContextMenuAction) -> Unit,
){
    var showContextMenu by remember { mutableStateOf(false) }
    var cardPosition by remember { mutableStateOf(Offset.Zero) }
    var cardSize by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
            val positionInWindow = coordinates.positionInWindow()
            cardPosition = Offset(positionInWindow.x, positionInWindow.y)
            cardSize = Offset(
                coordinates.size.width.toFloat(),
                coordinates.size.height.toFloat()
            )
    }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(59 / 73f)
                .combinedClickable(
                    onClick = {
                        showContextMenu = true
                    },
                    onDoubleClick = {
                        onDoubleClick(collectionItem.id.gameId)
                    }
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_game_placeholder),
                    contentDescription = collectionItem.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    val containerWidthDp = maxWidth

                    AdaptiveTextForGameTile(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        text = collectionItem.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        containerWidthDp = containerWidthDp,
                        containerPaddingDp = 8.dp
                    )
                }
            }
        }

        if (showContextMenu){
            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp

            val menuX = (if (cardPosition.x + cardSize.x + 200.dp.value < screenWidth.value) {
                (cardPosition.x + cardSize.x + 8.dp.value).dp
            } else if (cardPosition.x - 200.dp.value > 0) {
                (cardPosition.x - 200.dp.value - 8.dp.value).dp
            } else {
                cardPosition.x.dp
            })

            val menuY = (if (cardPosition.y + 300.dp.value < screenHeight.value) {
                cardPosition.y.dp
            } else {
                (cardPosition.y - 200.dp.value).dp
            })

            DropdownMenu(
                expanded = showContextMenu,
                onDismissRequest = { showContextMenu = false },
                offset = DpOffset(menuX, menuY),
                properties = PopupProperties(focusable = true),
                containerColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 3.dp
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Status: ${collectionItem.status}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                           }, // TODO: change to const string from values.xml
                    onClick = {
                        onContextMenu(collectionItem.id, ContextMenuAction.onStatusClick)
                        showContextMenu = false
                    },
                    leadingIcon = {
                        Icon(
                            Icons.AutoMirrored.Filled.MenuOpen,
                            contentDescription = "Menu item: status ${collectionItem.status}",
                            modifier = Modifier.size(18.dp),
                            tint = PolkaAcceptButtonColors.contentColor
                        )
                    }
                )

                HorizontalDivider()

                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Your rating: ${collectionItem.rating ?: "N/A"}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                           }, // TODO: change to const string from values.xml
                    onClick = {
                        onContextMenu(collectionItem.id, ContextMenuAction.onRatingClick)
                        showContextMenu = false
                    },
                    leadingIcon = {
                        Icon(
                            if (collectionItem.rating == null) Icons.Filled.StarBorder else
                                Icons.Filled.Star,
                            contentDescription = "Your rating: ${collectionItem.rating ?: "not stated"}",
                            modifier = Modifier.size(18.dp),
                            tint = PolkaAcceptButtonColors.contentColor
                        )
                    }
                )

                HorizontalDivider()

                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Add session",
                            style = MaterialTheme.typography.bodyLarge
                        )
                           }, // TODO: change to const string from values.xml
                    onClick = {
                        onContextMenu(collectionItem.id, ContextMenuAction.onAddSessionClick)
                        showContextMenu = false
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.AddCircleOutline,
                            contentDescription = "Add session",
                            modifier = Modifier.size(18.dp),
                            tint = PolkaAcceptButtonColors.contentColor
                        )
                    }
                )
            }
        }
    }
}

@Preview( // TIP: only for preview
    name = "GameTile Row (3 items)",
    showBackground = true,
    backgroundColor = 0xFFF5F5F5,
    device = Devices.PIXEL_4
)
@Composable
fun PreviewGameRowTile() {
    PolkaTheme {
        val testGames = listOf(
            CollectionItem(
                name = "Catan",
                id = CollectionItem.Id(1, 1),
                status = "Own",
                rating = 8
            ),
            CollectionItem(
                name = "Code Names",
                id = CollectionItem.Id(1, 1),
                status = "Wishlist",
                rating = null
            ),
            CollectionItem(
                name = "Ticket to Ride",
                id = CollectionItem.Id(1, 1),
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
                testGames.forEach { collectionItem ->
                    GameTile(
                        collectionItem = collectionItem,
                        modifier = Modifier.weight(1f),
                        onDoubleClick = { /* заглушка */ },
                        onContextMenu = { _, _ -> /* заглушка */ },
                    )
                }
            }
        }
    }
}
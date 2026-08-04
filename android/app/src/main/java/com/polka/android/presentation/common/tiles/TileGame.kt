package com.polka.android.presentation.common.tiles

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MenuOpen
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
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.polka.android.R
import com.polka.android.presentation.theme.PolkaButtonAcceptColors
import com.polka.android.presentation.theme.PolkaTheme
import com.polka.android.utils.AdaptiveTextForTileGame
import kotlin.math.roundToInt

sealed class ContextMenuAction {
    object onStatusClick : ContextMenuAction()
    object onRatingClick : ContextMenuAction()
    object onAddSessionClick : ContextMenuAction()
}

data class Game(
    var gameId: Int,
    var collectionItemId: Int,
    var gameImageUrl: String,
    var gameName: String,
    var gameStatus: String,
    var userRating: Int?
)

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun TileGame(
    game: Game,
    modifier: Modifier = Modifier,
    onDoubleClick: (Int) -> Unit,
    onContextMenu: (Int, ContextMenuAction) -> Unit,
    onDragStart: (Int) -> Unit
){
    var showContextMenu by remember { mutableStateOf(false) }
    var cardPosition by remember { mutableStateOf(Offset.Zero) }
    var cardSize by remember { mutableStateOf(Offset.Zero) }

    Box(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(59 / 73f)
                .combinedClickable(
                    onClick = {
                        showContextMenu = true
                    },
                    onDoubleClick = {
                        onDoubleClick(game.gameId)
                    },
                    onLongClick = {
                        onDragStart(game.collectionItemId)
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
                    contentDescription = game.gameName,
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

                    AdaptiveTextForTileGame(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        text = game.gameName,
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
                            text = "Status: ${game.gameStatus}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                           }, // TODO: change to const string from values.xml
                    onClick = {
                        onContextMenu(game.collectionItemId, ContextMenuAction.onStatusClick)
                        showContextMenu = false
                    },
                    leadingIcon = {
                        Icon(
                            Icons.AutoMirrored.Filled.MenuOpen,
                            contentDescription = "Menu item: status ${game.gameStatus}",
                            modifier = Modifier.size(18.dp),
                            tint = PolkaButtonAcceptColors.contentColor
                        )
                    }
                )

                HorizontalDivider()

                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Your rating: ${game.userRating ?: "N/A"}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                           }, // TODO: change to const string from values.xml
                    onClick = {
                        onContextMenu(game.collectionItemId, ContextMenuAction.onRatingClick)
                        showContextMenu = false
                    },
                    leadingIcon = {
                        Icon(
                            if (game.userRating == null) Icons.Filled.StarBorder else
                                Icons.Filled.Star,
                            contentDescription = "Your rating: ${game.userRating ?: "not stated"}",
                            modifier = Modifier.size(18.dp),
                            tint = PolkaButtonAcceptColors.contentColor
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
                        onContextMenu(game.collectionItemId, ContextMenuAction.onAddSessionClick)
                        showContextMenu = false
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.AddCircleOutline,
                            contentDescription = "Add session",
                            modifier = Modifier.size(18.dp),
                            tint = PolkaButtonAcceptColors.contentColor
                        )
                    }
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
fun PreviewGameTileRow() {
    PolkaTheme {
        val testGames = listOf(
            Game(
                gameId = 2,
                collectionItemId = 2,
                gameImageUrl = "https://cf.geekdo-images.com/7XkzUhj3LsqUFfATeIVjIA__itemrep/img/9N8tR4fX5k7Z2K1yQ0wP3oI8nJ6=/fit-in/246x300/filters:strip_icc()/pic2419375.jpg",
                gameName = "Catan",
                gameStatus = "Own",
                userRating = 6
            ),
            Game(
                gameId = 1,
                collectionItemId = 1,
                gameImageUrl = "https://cf.geekdo-images.com/6j5RxBvNS9c1HpVJk4WpZQ__itemrep/img/Kw7oP_0HSOlgH93Z8pN3wFjx3n0=/fit-in/246x300/filters:strip_icc()/pic5424479.jpg",
                gameName = "Dune",
                gameStatus = "Wishlist",
                userRating = null
            ),
            Game(
                gameId = 3,
                collectionItemId = 3,
                gameImageUrl = "https://cf.geekdo-images.com/4iJk9KJ0n7sQ8vM5wX3yFg__itemrep/img/8N7tR4fX5k7Z2K1yQ0wP3oI8nJ6=/fit-in/246x300/filters:strip_icc()/pic2437871.jpg",
                gameName = "Ticket to Ride",
                gameStatus = "Previous owned",
                userRating = 7
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
                testGames.forEach { game ->
                    TileGame(
                        game = game,
                        modifier = Modifier.weight(1f),
                        onDoubleClick = { /* заглушка */ },
                        onContextMenu = { _, _ -> /* заглушка */ },
                        onDragStart = { /* заглушка */ }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                testGames.forEach { game ->
                    TileGame(
                        game = game,
                        modifier = Modifier.weight(1f),
                        onDoubleClick = { /* заглушка */ },
                        onContextMenu = { _, _ -> /* заглушка */ },
                        onDragStart = { /* заглушка */ }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(13.dp)
            ) {
                testGames.forEach { game ->
                    TileGame(
                        game = game,
                        modifier = Modifier.weight(1f),
                        onDoubleClick = { /* заглушка */ },
                        onContextMenu = { _, _ -> /* заглушка */ },
                        onDragStart = { /* заглушка */ }
                    )
                }
            }
        }
    }
}
package com.polka.android.presentation.common.tiles

import android.R.attr.maxWidth
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polka.android.R
import com.polka.android.presentation.theme.PolkaTheme
import com.polka.android.utils.AdaptiveTextForTileGame

sealed class ContextMenuAction {
    object onStatusClick : ContextMenuAction()
    object onRatingClick : ContextMenuAction()
    object onAddSessionClick : ContextMenuAction()
}

data class Game(
    var gameId: Int,
    var collectionItemId: Int,
    var gameImageUrl: String,
    var gameName: String
)

@Composable
fun TileGame(
    game: Game,
    modifier: Modifier = Modifier,
    onDoubleClick: (Int) -> Unit,
    onContextMenu: (Int, ContextMenuAction) -> Unit,
    onDragStart: (Int) -> Unit
){
    var showContextMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(59/73f)
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
            containerColor = MaterialTheme.colorScheme.surface // TODO: change color
        )
    ){
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
                gameName = "Catan"
            ),
            Game(
                gameId = 1,
                collectionItemId = 1,
                gameImageUrl = "https://cf.geekdo-images.com/6j5RxBvNS9c1HpVJk4WpZQ__itemrep/img/Kw7oP_0HSOlgH93Z8pN3wFjx3n0=/fit-in/246x300/filters:strip_icc()/pic5424479.jpg",
                gameName = "Dune"
            ),
            Game(
                gameId = 3,
                collectionItemId = 3,
                gameImageUrl = "https://cf.geekdo-images.com/4iJk9KJ0n7sQ8vM5wX3yFg__itemrep/img/8N7tR4fX5k7Z2K1yQ0wP3oI8nJ6=/fit-in/246x300/filters:strip_icc()/pic2437871.jpg",
                gameName = "Ticket to Ride"
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
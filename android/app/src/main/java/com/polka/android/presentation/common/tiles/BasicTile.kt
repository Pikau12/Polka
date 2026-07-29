package com.polka.android.presentation.common.tiles

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.polka.android.R

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
fun GameTile(
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
            .aspectRatio(0.75f)
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
            AsyncImage(
                model = game.gameImageUrl,
                contentDescription = game.gameName,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.ic_game_placeholder),
                placeholder = painterResource(R.drawable.ic_loading)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = game.gameName,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
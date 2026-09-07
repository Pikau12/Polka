package com.polka.android.presentation.common.layout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.polka.android.presentation.common.UiConstants.STROKE_SIZE
import com.polka.android.presentation.common.tiles.BubbleType
import com.polka.android.presentation.common.tiles.SessionTile
import com.polka.android.presentation.model.SessionSummary
import com.polka.android.utils.GetSessionTileFormatByElementsWidth

@Composable
fun SessionsLayout (
    sessions: List<SessionSummary>,
    onAddSessionClick: () -> Unit,
    onSessionClick: (Long) -> Unit,
    paddingValues: PaddingValues,
) {
    val sessionsState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        state = sessionsState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp) // maybe change value
    ) {
        item (key = -1) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable(onClick = onAddSessionClick),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    STROKE_SIZE,
                    MaterialTheme.colorScheme.surface
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add session tile",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        items(items = sessions, key = { it.sessionId }) { item ->
            val elements: MutableList<Pair<String, BubbleType>> = emptyList<Pair<String, BubbleType>>().toMutableList()

            elements.add(Pair(item.gameName, BubbleType.NAME))
            elements.add(Pair(item.date.toString(), BubbleType.DATE))
            if (item.place != null)
                elements.add(Pair(item.place, BubbleType.PLACE))
            if (item.winners != null && item.players != null) { // if winners exists players must be existed!
                for (player in item.players) {
                    if (item.winners[player] == true)
                        elements.add(Pair(player, BubbleType.WINNER))
                }

                for (player in item.players) {
                    if (item.winners[player] == false)
                        elements.add(Pair(player, BubbleType.PLAYER))
                }
            }

            // TODO : add duration Bubble

            SessionTile(
                session = item,
                onClick = { onSessionClick(item.sessionId) },
                format = GetSessionTileFormatByElementsWidth(
                    cardWidth = 10.dp, // TODO: change
                    fieldWidth = 10.dp, // TODO: change
                    elements = elements
                )
            )
        }
    }
}
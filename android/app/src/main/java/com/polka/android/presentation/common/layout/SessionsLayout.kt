package com.polka.android.presentation.common.layout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polka.android.presentation.common.UiConstants.STROKE_SIZE
import com.polka.android.presentation.common.tiles.BubbleType
import com.polka.android.presentation.common.tiles.SessionTile
import com.polka.android.presentation.model.SessionSummary
import com.polka.android.presentation.theme.ManropeFontFamily
import com.polka.android.presentation.theme.PolkaTheme
import com.polka.android.utils.GetSessionTileFormatByElementsWidth

@Composable
fun SessionsLayout(
    sessions: List<SessionSummary>,
    onAddSessionClick: () -> Unit,
    onSessionClick: (Long) -> Unit,
) {
    val sessionsState = rememberLazyListState()
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current.density

    Scaffold(
        containerColor = Color.Transparent
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            state = sessionsState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            item(key = -1) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
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
            }

            items(items = sessions, key = { it.sessionId }) { item ->
                val elements: MutableList<Pair<String, BubbleType>> = mutableListOf()

                elements.add(Pair(item.gameName, BubbleType.NAME))
                elements.add(Pair(item.date, BubbleType.DATE))
                if (item.duration != null)
                    elements.add(
                        Pair(
                            item.duration,
                            BubbleType.DURATION
                        )
                    )
                if (item.place != null)
                    elements.add(Pair(item.place, BubbleType.PLACE))
                if (item.winners != null && item.players != null) {
                    for (player in item.players) {
                        if (item.winners[player] == true)
                            elements.add(Pair(player, BubbleType.WINNER))
                    }
                    for (player in item.players) {
                        if (item.winners[player] == false)
                            elements.add(Pair(player, BubbleType.PLAYER))
                    }
                }

                BoxWithConstraints(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val imageWidthSmall = 38.dp
                    val imageWidthMedium = 82.dp
                    val spacing = 6.dp
                    val cardPadding = 6.dp
                    val cardWidth = maxWidth

                    val fieldWidthWithSmallPicture =
                        cardWidth - imageWidthSmall - spacing - cardPadding * 2
                    val fieldWidthWithMediumPicture =
                        cardWidth - imageWidthMedium - spacing - cardPadding * 2

                    val elementsWithWidth = elements.map { (text, type) ->
                        val textWidthPx = textMeasurer.measure(
                            text = text,
                            style = TextStyle(
                                fontFamily = ManropeFontFamily,
                                fontWeight = when (type) {
                                    BubbleType.NAME -> FontWeight.SemiBold
                                    else -> FontWeight.Medium
                                },
                                fontSize = 16.sp
                            )
                        ).size.width
                        val textWidthDp = with(LocalDensity.current) {
                            (textWidthPx / density).dp
                        }
                        Triple(text, type, textWidthDp.value)
                    }

                    SessionTile(
                        session = item,
                        onClick = { onSessionClick(item.sessionId) },
                        format = GetSessionTileFormatByElementsWidth(
                            cardWidth = cardWidth,
                            fieldWidthWithSmallPicture = fieldWidthWithSmallPicture,
                            fieldWidthWithMediumPicture = fieldWidthWithMediumPicture,
                            elements = elementsWithWidth
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun SessionsLayoutPreview() {
    PolkaTheme {
        val mockSessions = listOf(
            SessionSummary(
                sessionId = 1L,
                gameName = "Two rooms and a boom",
                gameImage = null,
                date = "01.09.26",
                duration = "2 h",
                place = "Home",
                players = listOf("Phil", "Joseph", "April", "Harmon", "Billy", "Mr Bones", "Kurash Bambei"),
                winners = mapOf(
                    "Phil" to true,
                    "Joseph" to false,
                    "April" to false,
                    "Harmon" to false,
                    "Billy" to false,
                    "Mr Bones" to false,
                    "Kurash Bambei" to false
                )
            ),
            SessionSummary(
                sessionId = 2L,
                gameName = "Catan",
                gameImage = null,
                date = "02.09.26",
                duration = "1.5 h",
                place = "Cafe",
                players = listOf("Player1", "Player2"),
                winners = mapOf("Player1" to true, "Player2" to false)
            ),
            SessionSummary(
                sessionId = 3L,
                gameName = "Chess",
                gameImage = null,
                date = "03.09.26",
                duration = null,
                place = null,
                players = null,
                winners = null
            )
        )

        SessionsLayout(
            sessions = mockSessions,
            onAddSessionClick = { },
            onSessionClick = { },
        )
    }
}
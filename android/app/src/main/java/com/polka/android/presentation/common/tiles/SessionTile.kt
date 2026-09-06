package com.polka.android.presentation.common.tiles

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.polka.android.R
import com.polka.android.presentation.common.UiConstants
import com.polka.android.presentation.common.UiConstants.SESSION_TILE_SINGLE_ROW_HEIGHT
import com.polka.android.presentation.common.UiConstants.SESSION_TILE_THREE_ROWS_HEIGHT
import com.polka.android.presentation.common.UiConstants.SESSION_TILE_TWO_ROWS_HEIGHT
import com.polka.android.presentation.model.SessionSummary
import com.polka.android.presentation.theme.ManropeFontFamily
import com.polka.android.presentation.theme.PolkaBubbleDate
import com.polka.android.presentation.theme.PolkaBubbleName
import com.polka.android.presentation.theme.PolkaBubblePlace
import com.polka.android.presentation.theme.PolkaBubblePlayer
import com.polka.android.presentation.theme.PolkaBubbleWinner
import com.polka.android.presentation.theme.PolkaOnBubble
import com.polka.android.presentation.theme.PolkaSessionCardColors

enum class BubbleType {
    NAME, DATE, PLACE, WINNER, PLAYER
}

@Composable
fun TextBubbleTemplate(
    text: String,
    color: Color,
    fontWeight: FontWeight = FontWeight.Medium,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .height(38.dp)
            .padding(8.dp)
            .background(
                color = color,
                shape = RoundedCornerShape(16.dp)
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let { // TOOD: change icon visualization (it need to be under text)
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = text,
                color = PolkaOnBubble,
                style = TextStyle(
                    fontFamily = ManropeFontFamily,
                    fontWeight = fontWeight,
                    fontSize = 16.sp
                )
            )
        }
    }
}

@Composable
fun TextBubble(
    type: BubbleType,
    text: String
) {
    val (color, weight) = when (type) {
        BubbleType.NAME -> PolkaBubbleName to FontWeight.SemiBold
        BubbleType.DATE -> PolkaBubbleDate to FontWeight.Medium
        BubbleType.PLACE -> PolkaBubblePlace to FontWeight.Medium
        BubbleType.WINNER -> PolkaBubbleWinner to FontWeight.Medium
        BubbleType.PLAYER -> PolkaBubblePlayer to FontWeight.Medium
    }

    TextBubbleTemplate(
        text = text,
        color = color,
        fontWeight = weight
    )
}

data class SessionTileFormat(
    val numOfRows: Int,
    val firstRow: List<Pair<String, BubbleType>> = emptyList(),
    val secondRow: List<Pair<String, BubbleType>> = emptyList(),
    val thirdRow: List<Pair<String, BubbleType>> = emptyList()
)

@Composable
fun SessionTile(
    session: SessionSummary,
    onClick: () -> Unit,
    format: SessionTileFormat
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(getTileHeight(format.numOfRows))
            .padding(6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = PolkaSessionCardColors
    ) {
        when (format.numOfRows) {
            1 -> SingleRowTile(session, format.firstRow)
            2 -> TwoRowTile(session, format)
            3 -> ThreeRowTile(session, format)
            else -> error("Invalid value for SessionTileFormat.numOfRows in SessionTile")
        }
    }
}

private fun getTileHeight(numOfRows: Int) = when (numOfRows) {
    1 -> SESSION_TILE_SINGLE_ROW_HEIGHT
    2 -> SESSION_TILE_TWO_ROWS_HEIGHT
    3 -> SESSION_TILE_THREE_ROWS_HEIGHT
    else -> error("Invalid numOfRows: $numOfRows")
}

@Composable
private fun SessionImage(
    session: SessionSummary,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = session.gameImage,
        placeholder = painterResource(R.drawable.ic_game_placeholder),
        contentDescription = session.gameName,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun BubblesRow(
    pairs: List<Pair<String, BubbleType>>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        for (pair in pairs) {
            TextBubble(
                text = pair.first,
                type = pair.second
            )
        }
    }
}

@Composable
private fun SingleRowTile(
    session: SessionSummary,
    firstRow: List<Pair<String, BubbleType>>
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start)
    ) {
        SessionImage(
            session = session,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
        )
        BubblesRow(pairs = firstRow)
    }
}

@Composable
private fun TwoRowTile(
    session: SessionSummary,
    format: SessionTileFormat
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start)
    ) {
        SessionImage(
            session = session,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            BubblesRow(
                pairs = format.firstRow,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            )
            BubblesRow(
                pairs = format.secondRow,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            )
        }
    }
}

@Composable
private fun ThreeRowTile(
    session: SessionSummary,
    format: SessionTileFormat
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(2f / 3f),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start)
        ) {
            SessionImage(
                session = session,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                BubblesRow(
                    pairs = format.firstRow,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                )
                BubblesRow(
                    pairs = format.secondRow,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                )
            }
        }
        BubblesRow(
            pairs = format.thirdRow,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f / 3f)
        )
    }
}
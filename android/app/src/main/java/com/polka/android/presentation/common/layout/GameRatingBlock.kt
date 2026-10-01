package com.polka.android.presentation.common.layout

import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polka.android.R
import com.polka.android.presentation.common.UiConstants.RATING_ICON_SIZE
import com.polka.android.presentation.common.UiConstants.SMALL_ICON_BACKGROUND_SIZE
import com.polka.android.presentation.common.UiConstants.SPACE_BETWEEN_RATING_AND_STAR_ICON
import com.polka.android.presentation.common.icons.SmallIcon
import com.polka.android.presentation.theme.PolkaRatingBackground
import com.polka.android.presentation.theme.PolkaRatingHigherBound
import com.polka.android.presentation.theme.PolkaRatingLowerBound
import com.polka.android.presentation.theme.PolkaTheme
import com.polka.android.utils.colorOverlay

@Composable
fun GameRatingBlock(
    bggAverageRating: Double,
    polkaAverageRating: Double,
    bggNumberOfRatings: String,
    polkaNumberOfRatings: String,
    userRating: Int?
) {
    TableCard {
        TableRow {
            TableCell {
                SmallIcon(
                    icon = R.drawable.rating_icon,
                    iconSize = RATING_ICON_SIZE,
                    backgroundColor = PolkaRatingBackground
                )

                Text(
                    "Rating",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            TableCell(
                "Average",
                style = MaterialTheme.typography.bodyLarge
            )
            TableCell(
                "Count",
                style = MaterialTheme.typography.bodyLarge,
                isLast = true
            )
        }
        TableRow {
            TableCell {
                Image(
                    painter = painterResource(R.drawable.bgg_icon),
                    contentDescription = "BGG icon",
                    modifier = Modifier.size(
                        height = 29.dp, width = 62.dp
                    )
                )
            }

            TableCell(horizontalSpacedBy = SPACE_BETWEEN_RATING_AND_STAR_ICON){
                Text(
                    bggAverageRating.toString(),
                    style = MaterialTheme.typography.titleSmall,
                )

                Icon(
                    Icons.Filled.Star,
                    contentDescription = "Star",
                    modifier = Modifier.size(20.dp),
                    tint = colorOverlay(
                        colorBack = PolkaRatingLowerBound,
                        colorFront = PolkaRatingHigherBound,
                        alpha = ((bggAverageRating - 1) / 9).toFloat()
                    )
                )
            }

            TableCell(
                "201233",
                style = MaterialTheme.typography.titleSmall,
                isLast = true
            )
        }
        TableRow {
            TableCell {
                Image(
                    painter = painterResource(R.drawable.polka_icon),
                    contentDescription = "Polka icon",
                    modifier = Modifier.size(SMALL_ICON_BACKGROUND_SIZE)
                )

                Text(
                    "Polka",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            TableCell(horizontalSpacedBy = SPACE_BETWEEN_RATING_AND_STAR_ICON){
                Text(
                    polkaAverageRating.toString(),
                    style = MaterialTheme.typography.titleSmall,
                )

                Icon(
                    Icons.Filled.Star,
                    contentDescription = "Star",
                    modifier = Modifier.size(20.dp),
                    tint = colorOverlay(
                        colorBack = PolkaRatingLowerBound,
                        colorFront = PolkaRatingHigherBound,
                        alpha = ((polkaAverageRating - 1) / 9).toFloat()
                    )
                )
            }

            TableCell(
                "109",
                style = MaterialTheme.typography.titleSmall,
                isLast = true
            )
        }
        TableRow(showDivider = false) {
            TableCell(
                "Your",
                style = MaterialTheme.typography.titleMedium
            )
            TableCell(
                "9",
                style = MaterialTheme.typography.titleSmall,
                weight = 2f,
                isLast = true
            )
        }
    }
}

@Composable
fun TableCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            content = content
        )
    }
}

@Composable
fun ColumnScope.TableRow(
    showDivider: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Column(
        modifier = Modifier.weight(1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
        if (showDivider) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@Composable
fun RowScope.TableCell(
    weight: Float = 1f,
    isLast: Boolean = false,
    horizontalSpacedBy: Dp = 10.dp,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .weight(weight)
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row (
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacedBy, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
        if (!isLast) {
            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float = 1f,
    isLast: Boolean = false,
    style: TextStyle
) {
    TableCell(weight = weight, isLast = isLast) {
        Text(
            text,
            style = style
        )
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 400)
@Composable
fun GameInfoCardPreview() {
    PolkaTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            GameRatingBlock(
                bggAverageRating = 9.0,
                polkaAverageRating = 8.3,
                bggNumberOfRatings = "201233",
                polkaNumberOfRatings = "109",
                userRating = 9
            )
        }
    }
}
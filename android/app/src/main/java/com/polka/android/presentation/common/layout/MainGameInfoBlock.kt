package com.polka.android.presentation.common.layout

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.polka.android.R
import com.polka.android.presentation.theme.PolkaAgeRestrictionBackground
import com.polka.android.presentation.theme.PolkaMainGameInfoBlockColors
import com.polka.android.presentation.theme.PolkaPlayerCountBackground
import com.polka.android.presentation.theme.PolkaReleaseYearBackground
import com.polka.android.presentation.theme.PolkaTheme
import com.polka.android.presentation.theme.PolkaTimeRangeBackground
import com.polka.android.presentation.theme.PolkaWeightBackground
import com.polka.android.presentation.theme.PolkaWeightHard
import com.polka.android.utils.colorOverlay


@Composable
fun MainGameInfoBlock (
    image: ImageRequest?,
    releaseYear: String?,
    playerCount: String?,
    bestPlayerCount: String?,
    timeRange: String?,
    averageSessionTime: String?,
    ageRestriction: String?,
    weight: Float?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(16.dp),
        colors = PolkaMainGameInfoBlockColors
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            if (image != null) {
                AsyncImage(
                    model = image,
                    contentDescription = "Game image",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_game_placeholder),
                    contentDescription = "Game image",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ImageInBoxAndTextRow(
                    image = R.drawable.calendar_icon,
                    imageSize = 20.dp,
                    text = "Release: ${releaseYear?: "N/A"}",
                    iconBackgroundColor = PolkaReleaseYearBackground
                )

                ImageInBoxAndTextRow(
                    image = R.drawable.players_count_icon,
                    imageSize = 20.dp,
                    text = "${playerCount?: "N/A"}${if (bestPlayerCount != null) " (Best: $bestPlayerCount)" else ""}",
                    iconBackgroundColor = PolkaPlayerCountBackground
                )

                ImageInBoxAndTextRow(
                    image = R.drawable.time_range_icon,
                    imageSize = 22.dp,
                    text = "${timeRange?: "N/A"}${if (averageSessionTime != null) " (Average: $averageSessionTime)" else ""}",
                    iconBackgroundColor = PolkaTimeRangeBackground
                )

                AgeRestrictionAndWeightRow(
                    ageRestriction = ageRestriction?: "N/A",
                    weight = weight
                )
            }
        }
    }
}

@Composable
fun ImageInBoxAndTextRow(
    @DrawableRes image: Int,
    imageSize: Dp,
    text: String,
    iconBackgroundColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = "", // TODO : ???
                modifier = Modifier.size(imageSize),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

@Composable
fun AgeRestrictionAndWeightRow(
    ageRestriction: String,
    weight: Float?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PolkaAgeRestrictionBackground),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.age_restriction_icon),
                    contentDescription = "", // TODO : ???
                    modifier = Modifier.size(22.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = ageRestriction,
                style = MaterialTheme.typography.titleSmall,
            )
        }

        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PolkaWeightBackground),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.weight_icon),
                    contentDescription = "", // TODO : ???
                    modifier = Modifier.size(20.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = buildAnnotatedString {
                    if (weight != null) {
                        withStyle(
                            SpanStyle(
                                color = colorOverlay(
                                    colorBack = MaterialTheme.colorScheme.onSurface,
                                    colorFront = PolkaWeightHard,
                                    alpha = weight / 5
                                )
                            )
                        ) {
                            append(weight.toString())
                        }

                        append(" / 5")
                    }
                    else
                        append("N/A")
                },
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun MainGameInfoBlockPreview() {
    PolkaTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            MainGameInfoBlock(
                image = null,
                releaseYear = null,
                playerCount = "1 - 5",
                bestPlayerCount = "3",
                timeRange = "1 - 1.5 h",
                averageSessionTime = "1.5 h",
                ageRestriction = "12+",
                weight = 4f
            )
        }
    }
}
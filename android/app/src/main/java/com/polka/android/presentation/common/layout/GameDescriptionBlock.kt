package com.polka.android.presentation.common.layout

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polka.android.presentation.theme.PolkaGameDescriptionBlockColors
import com.polka.android.presentation.theme.PolkaTheme

@Composable
fun GameDescriptionBlock(
    inDetail: Boolean = false,
    onClick: () -> Unit,
    shortDescription: String,
    fullDescription: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = PolkaGameDescriptionBlockColors
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp)
                .animateContentSize(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable(onClick = onClick)
                    .padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = if (!inDetail) Icons.Filled.KeyboardArrowDown
                    else Icons.Filled.KeyboardArrowUp,
                    contentDescription = if (!inDetail) "See more button" else "See less button",
                    modifier = Modifier.size(24.dp),
                )

                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleLarge
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = if (!inDetail) "Short" else "Full",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Crossfade(
                targetState = inDetail,
                animationSpec = tween(300),
                label = "description"
            ) { expanded ->
                Box(
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (!expanded) shortDescription else fullDescription,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 400)
@Composable
fun GameDescriptionBlockPreview() {
    var inDetail by remember { mutableStateOf(false) }

    PolkaTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            GameDescriptionBlock(
                inDetail = inDetail,
                onClick = { inDetail = !inDetail },
                shortDescription = "Decide the fate of the woodland as asymmetric factions fight for contrasting goals. ",
                fullDescription = "Root is a game of adventure and war in which 2 to 4 (1 to 6 with the 'Riverfolk' expansion, 2-6 with the 'Underworld', or 'Marauder' expansions) players battle for control of a vast wilderness. Like Vast: The Crystal Caverns, each player in Root has unique capabilities and a different victory condition. Now, with the aid of gorgeous, multi-use cards, a truly asymmetric design has never been more accessible."
            )
        }
    }
}


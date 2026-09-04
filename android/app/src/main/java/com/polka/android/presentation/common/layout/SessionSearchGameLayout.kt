package com.polka.android.presentation.common.layout

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.polka.android.R
import com.polka.android.presentation.common.inputs.SearchWithoutBody
import com.polka.android.presentation.model.CollectionItemSummary
import com.polka.android.presentation.theme.PolkaGameSearchCardColors
import com.polka.android.presentation.theme.PolkaStar
import com.polka.android.presentation.theme.PolkaUserRatingStar

@Composable
fun SessionSearchGameLayout (
    enabled: Boolean,
    onBackClick: () -> Unit,
    onSearchChange: (String) -> Unit,
    @DrawableRes backgroundImageId: Int,
    query: String,
    placeholderText: String,
    collection: List<CollectionItemSummary>
) {
    BackHandler(enabled) {
        onBackClick()
    }

    if (enabled) {
        Box {
            Image(
                painter = painterResource(backgroundImageId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    SearchWithoutBody(
                        query = query,
                        onValueChange = onSearchChange,
                        placeholderText = placeholderText,
                    )
                },
                containerColor = Color.Transparent
            ) { paddingValues ->
                val collectionState = rememberLazyListState()

                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    state = collectionState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(13.dp) // maybe change value
                ) {
                    items(items = collection, key = { it.id.gameId }) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(94.dp)
                                .padding(6.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = PolkaGameSearchCardColors
                        ) {
                            Row (
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start)
                            ) {
                                AsyncImage(
                                    model = item.image,
                                    placeholder = painterResource(R.drawable.ic_game_placeholder),
                                    contentDescription = item.name,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .aspectRatio(1f),
                                    contentScale = ContentScale.Crop
                                )

                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically)
                                ) {
                                    Text(
                                        text = "${item.name} (${item.releaseYear})",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyLarge // maybe change
                                    )

                                    Row (
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start)
                                    ) {
                                        if (item.communityRating != null) {
                                            Row(
                                                modifier = Modifier.fillMaxHeight(),
                                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                                            ) {
                                                Icon(
                                                    Icons.Filled.StarRate,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(20.dp),
                                                    tint = PolkaStar
                                                )

                                                Text(
                                                    text = item.communityRating.toString(),
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    style = MaterialTheme.typography.bodyMedium // maybe change
                                                )
                                            }
                                        }

                                        if (item.userRating != null) {
                                            Row(
                                                modifier = Modifier.fillMaxHeight(),
                                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                                            ) {
                                                Icon(
                                                    Icons.Filled.StarRate,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(20.dp),
                                                    tint = PolkaUserRatingStar
                                                )

                                                Text(
                                                    text = item.userRating.toString(),
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    style = MaterialTheme.typography.bodyMedium // maybe change
                                                )
                                            }
                                        }

                                        if (item.designer != null) {
                                            Text (
                                                text = "Designer: ${item.designer}",
                                                color = MaterialTheme.colorScheme.onSurface,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
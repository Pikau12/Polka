package com.polka.android.presentation.common.tiles

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import coil3.compose.AsyncImage
import com.polka.android.R
import com.polka.android.presentation.common.UiConstants.MAIN_CORNER_RADIUS
import com.polka.android.presentation.model.CollectionItem
import com.polka.android.presentation.theme.PolkaAcceptButtonColors
import com.polka.android.utils.AdaptiveTextForGameTile

sealed class ContextMenuAction {
    object onStatusClick : ContextMenuAction()
    object onRatingClick : ContextMenuAction()
    object onAddSessionClick : ContextMenuAction()
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun GameTile(
    collectionItem: CollectionItem,
    modifier: Modifier = Modifier,
    onDoubleClick: (Long) -> Unit,
    onContextMenu: (CollectionItem.Id, ContextMenuAction) -> Unit,
) {
    var showContextMenu by remember { mutableStateOf(false) }
    var cardPosition by remember { mutableStateOf(Offset.Zero) }
    var cardSize by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                val positionInWindow = coordinates.positionInWindow()
                cardPosition = Offset(positionInWindow.x, positionInWindow.y)
                cardSize = Offset(
                    coordinates.size.width.toFloat(),
                    coordinates.size.height.toFloat()
                )
            }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(59 / 73f)
                .combinedClickable(
                    onClick = { showContextMenu = true },
                    onDoubleClick = { onDoubleClick(collectionItem.id.gameId) }
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
                if (collectionItem.image != null) {
                    AsyncImage(
                        model = collectionItem.image,
                        contentDescription = "Game image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(MAIN_CORNER_RADIUS)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.ic_game_placeholder),
                        contentDescription = collectionItem.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(MAIN_CORNER_RADIUS)),
                        contentScale = ContentScale.Crop
                    )
                }

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    val containerWidthDp = maxWidth

                    AdaptiveTextForGameTile(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        text = collectionItem.name,
                        color = MaterialTheme.colorScheme.onSurface,
                        containerWidthDp = containerWidthDp,
                        containerPaddingDp = 8.dp
                    )
                }
            }
        }

        if (showContextMenu) {
            val menuWidth = 200.dp
            val gap = 8.dp
            val density = LocalDensity.current

            val positionProvider = remember(cardPosition, cardSize) {
                object : PopupPositionProvider {
                    override fun calculatePosition(
                        anchorBounds: IntRect,
                        windowSize: IntSize,
                        layoutDirection: LayoutDirection,
                        popupContentSize: IntSize
                    ): IntOffset {
                        val menuWidthPx = with(density) { menuWidth.toPx() }
                        val gapPx = with(density) { gap.toPx() }
                        val screenWidthPx = windowSize.width.toFloat()
                        val screenHeightPx = windowSize.height.toFloat()

                        val cardX = cardPosition.x
                        val cardY = cardPosition.y
                        val cardWidth = cardSize.x
                        val cardHeight = cardSize.y
                        val menuHeightPx = popupContentSize.height.toFloat()

                        // 1. right
                        val rightX = cardX + cardWidth + gapPx
                        val yRight = cardY.coerceIn(0f, (screenHeightPx - menuHeightPx).coerceAtLeast(0f))
                        if (rightX + menuWidthPx <= screenWidthPx) {
                            return IntOffset(rightX.toInt(), yRight.toInt())
                        }

                        // 2. left
                        val leftX = cardX - menuWidthPx - gapPx
                        val yLeft = cardY.coerceIn(0f, (screenHeightPx - menuHeightPx).coerceAtLeast(0f))
                        if (leftX >= 0f) {
                            return IntOffset(leftX.toInt(), yLeft.toInt())
                        }

                        // 3. bottom
                        val bottomY = cardY + cardHeight + gapPx
                        val xBottom = (cardX + cardWidth / 2f - menuWidthPx / 2f)
                            .coerceIn(0f, (screenWidthPx - menuWidthPx).coerceAtLeast(0f))
                        if (bottomY + menuHeightPx <= screenHeightPx) {
                            return IntOffset(xBottom.toInt(), bottomY.toInt())
                        }

                        // 4. top
                        val topY = cardY - menuHeightPx - gapPx
                        val xTop = (cardX + cardWidth / 2f - menuWidthPx / 2f)
                            .coerceIn(0f, (screenWidthPx - menuWidthPx).coerceAtLeast(0f))
                        if (topY >= 0f) {
                            return IntOffset(xTop.toInt(), topY.toInt())
                        }

                        val fallbackX = (cardX + cardWidth + gapPx)
                            .coerceIn(0f, (screenWidthPx - menuWidthPx).coerceAtLeast(0f))
                        val fallbackY = cardY.coerceIn(0f, (screenHeightPx - menuHeightPx).coerceAtLeast(0f))
                        return IntOffset(fallbackX.toInt(), fallbackY.toInt())
                    }
                }
            }

            Popup(
                popupPositionProvider = positionProvider,
                onDismissRequest = { showContextMenu = false },
                properties = PopupProperties(focusable = true)
            ) {
                Surface(
                    modifier = Modifier.width(menuWidth),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 3.dp
                ) {
                    Column {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Status: ${collectionItem.status.toString()}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                onContextMenu(collectionItem.id, ContextMenuAction.onStatusClick)
                                showContextMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.AutoMirrored.Filled.MenuOpen,
                                    contentDescription = "Menu item: status ${collectionItem.status}",
                                    modifier = Modifier.size(18.dp),
                                    tint = PolkaAcceptButtonColors.contentColor
                                )
                            }
                        )

                        HorizontalDivider()

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Your rating: ${collectionItem.rating ?: "N/A"}",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                onContextMenu(collectionItem.id, ContextMenuAction.onRatingClick)
                                showContextMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    if (collectionItem.rating == null) Icons.Filled.StarBorder
                                    else Icons.Filled.Star,
                                    contentDescription = "Your rating: ${collectionItem.rating ?: "not stated"}",
                                    modifier = Modifier.size(18.dp),
                                    tint = PolkaAcceptButtonColors.contentColor
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
                            },
                            onClick = {
                                onContextMenu(collectionItem.id, ContextMenuAction.onAddSessionClick)
                                showContextMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.AddCircleOutline,
                                    contentDescription = "Add session",
                                    modifier = Modifier.size(18.dp),
                                    tint = PolkaAcceptButtonColors.contentColor
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
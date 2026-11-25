package com.polka.android.presentation.common.menus

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.polka.android.presentation.model.CollectionItem
import com.polka.android.presentation.theme.PolkaCheckBoxColors
import com.polka.android.presentation.theme.PolkaRadioButtonColors
import com.polka.android.presentation.theme.PolkaTheme

@Composable
fun CenteredDropdownMenu(
    modifier: Modifier = Modifier,
    showShadow: Boolean = true,
    shadowElevation: Dp = 4.dp,
    onClickInside: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .wrapContentWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClickInside
            )
            .then(
                if (showShadow) {
                    Modifier.shadow(
                        elevation = shadowElevation,
                        shape = RoundedCornerShape(4.dp),
                        ambientColor = Color.Black.copy(alpha = 0.2f),
                        spotColor = Color.Black.copy(alpha = 0.2f)
                    )
                } else {
                    Modifier
                }
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(8.dp),
        content = content
    )
}

@Composable
fun GameStatusMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onGameStatusItemClick: (CollectionItem.Status) -> Unit,
    choicedItems: Set<CollectionItem.Status>
) {
    var wishlistExpanded by remember { mutableStateOf(false) }

    if (!expanded) {
        wishlistExpanded = false
    }

    if (expanded) {
        Dialog(
            onDismissRequest = {
                if (wishlistExpanded) {
                    wishlistExpanded = false
                } else {
                    onDismissRequest()
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false,
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        if (wishlistExpanded) wishlistExpanded = false else onDismissRequest()
                    },
                contentAlignment = Alignment.Center
            ) {
                CenteredDropdownMenu(showShadow = true) {
                    for (status in CollectionItem.Status.entries.filter { it.toWishlist() == null }) {
                        Row(
                            modifier = Modifier
                                .wrapContentWidth()
                                .clickable { onGameStatusItemClick(status) }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = status in choicedItems,
                                onCheckedChange = { onGameStatusItemClick(status) },
                                colors = PolkaCheckBoxColors
                            )
                            Text(
                                text = status.toString(),
                                modifier = Modifier.padding(start = 8.dp),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .wrapContentWidth()
                            .clickable { wishlistExpanded = true }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = choicedItems.any { it.toWishlist() != null },
                            onCheckedChange = { wishlistExpanded = true },
                            colors = PolkaCheckBoxColors
                        )

                        var status: CollectionItem.Status? = null
                        for (wishlistStatus in CollectionItem.Status.Wishlist.entries) {
                            status = wishlistStatus.toStatus()
                            if (status in choicedItems) break else status = null
                        }

                        Text(
                            text = if (status != null) "W-list [${status.toWishlist()}]" else "Wishlist",
                            modifier = Modifier.padding(start = 8.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = "Wishlist options",
                            modifier = Modifier.padding(start = 8.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }

    if (wishlistExpanded) {
        Popup(
            popupPositionProvider = object : PopupPositionProvider {
                override fun calculatePosition(
                    anchorBounds: IntRect,
                    windowSize: IntSize,
                    layoutDirection: LayoutDirection,
                    popupContentSize: IntSize
                ): IntOffset {
                    return IntOffset(0, 0)
                }
            },
            onDismissRequest = { wishlistExpanded = false },
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { wishlistExpanded = false },
                contentAlignment = Alignment.Center
            ) {
                CenteredDropdownMenu(
                    showShadow = true,
                    shadowElevation = 12.dp,
                    modifier = Modifier.offset(y = 200.dp),
                    onClickInside = { }
                ) {
                    for (wishlistStatus in CollectionItem.Status.Wishlist.entries) {
                        val status = wishlistStatus.toStatus()
                        val isSelected = status in choicedItems

                        Row(
                            modifier = Modifier
                                .wrapContentWidth()
                                .clickable { onGameStatusItemClick(status) }
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onGameStatusItemClick(status) },
                                colors = PolkaRadioButtonColors
                            )
                            Text(
                                text = wishlistStatus.toString(),
                                modifier = Modifier.padding(start = 8.dp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun GameStatusMenuPreview() {
    var expanded by remember { mutableStateOf(true) }
    var selectedStatuses by remember { mutableStateOf(setOf<CollectionItem.Status>()) }

    PolkaTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { expanded = true },
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text("Open menu")
            }

            GameStatusMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                onGameStatusItemClick = { status ->
                    selectedStatuses = if (status in selectedStatuses) {
                        selectedStatuses - status
                    } else {
                        selectedStatuses + status
                    }
                },
                choicedItems = selectedStatuses
            )
        }
    }
}
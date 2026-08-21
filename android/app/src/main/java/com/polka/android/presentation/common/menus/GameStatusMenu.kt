package com.polka.android.presentation.common.menus

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.polka.android.presentation.model.CollectionItem
import com.polka.android.presentation.theme.PolkaCheckBoxColors
import com.polka.android.presentation.theme.PolkaTheme

@Composable
fun CenteredDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    if (expanded) {
        Popup(
            alignment = Alignment.Center,
            offset = IntOffset(offset.x.value.toInt(), offset.y.value.toInt()),
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(
                focusable = true,
                dismissOnClickOutside = true
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onDismissRequest() },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = modifier
                        .wrapContentWidth()
                        .clickable { }
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(8.dp),
                    content = content
                )
            }
        }
    }
}

@Composable
fun GameStatusMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onGameStatusItemClick: (CollectionItem.Status) -> Unit,
    choicedItems: Set<CollectionItem.Status>
) {
    var wishlistExpanded by remember { mutableStateOf(false) }

    CenteredDropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
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
                    color = MaterialTheme.colorScheme.onSurface
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
            Text(
                text = "Wishlist",
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "Wishlist options",
                modifier = Modifier.padding(start = 8.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }

    // Подменю Wishlist — отдельное, позиционируется ниже
    CenteredDropdownMenu(
        expanded = wishlistExpanded,
        onDismissRequest = { wishlistExpanded = false },
        offset = DpOffset(0.dp, 200.dp)
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
                    onClick = { onGameStatusItemClick(status) }
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

// TODO: must to fix submenu shadow problem
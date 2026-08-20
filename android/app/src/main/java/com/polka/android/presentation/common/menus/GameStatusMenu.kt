package com.polka.android.presentation.common.menus

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.polka.android.presentation.model.CollectionItem
import com.polka.android.presentation.theme.PolkaCheckBoxColors
import com.polka.android.presentation.theme.PolkaStar
import com.polka.android.presentation.theme.PolkaTheme

@Composable
fun GameStatusMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onGameStatusItemClick: (CollectionItem.Status) -> Unit,
    choicedItems: Set<CollectionItem.Status>
){
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        for (status in CollectionItem.Status.entries.filter { it.toWishlist() == null }) {
            DropdownMenuItem(
                text = {
                    Text(status.toString())
                },
                onClick = {
                    onGameStatusItemClick(status)
                },
                leadingIcon = {
                    Checkbox(
                        checked = status in choicedItems,
                        onCheckedChange = {
                            onGameStatusItemClick(status)
                        },
                        colors = PolkaCheckBoxColors
                    )
                }
            )
        }

        var wishlistExpanded by remember { mutableStateOf(false) }

        DropdownMenuItem(
            text = { Text("Wishlist") },
            onClick = { wishlistExpanded = true },
            leadingIcon = {
                Checkbox(
                    checked = choicedItems.any { it.toWishlist() != null },
                    onCheckedChange = null,
                    colors = PolkaCheckBoxColors
                )
            },
            trailingIcon = {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "Wishlist options"
                )
            }
        )

        DropdownMenu(
            expanded = wishlistExpanded,
            onDismissRequest = { wishlistExpanded = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            for (wishlistStatus in CollectionItem.Status.Wishlist.entries) {
                val status = wishlistStatus.toStatus()
                DropdownMenuItem(
                    text = { Text(wishlistStatus.toString()) },
                    onClick = {
                        onGameStatusItemClick(status)
                        wishlistExpanded = false
                    }
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
            // Кнопка, к которой привязано меню
            Box {
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
}
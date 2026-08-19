package com.polka.android.presentation.common.menus

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.polka.android.presentation.model.CollectionItem
import com.polka.android.presentation.theme.PolkaStar

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
                    Checkbox( // TODO: add colors
                        checked = status in choicedItems,
                        onCheckedChange = {
                            onGameStatusItemClick(status)
                        }
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
                    onCheckedChange = null
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
                    },
                    leadingIcon = {
                        Checkbox(
                            checked = status in choicedItems,
                            onCheckedChange = { onGameStatusItemClick(status) }
                        )
                    }
                )
            }
        }
    }
}
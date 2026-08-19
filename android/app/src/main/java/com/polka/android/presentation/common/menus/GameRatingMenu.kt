package com.polka.android.presentation.common.menus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.contextmenu.modifier.appendTextContextMenuComponents
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.polka.android.presentation.common.buttons.CancelButton
import com.polka.android.presentation.common.buttons.TipButton
import com.polka.android.presentation.theme.PolkaAcceptButtonColors
import com.polka.android.presentation.theme.PolkaStar

@Composable
fun GameRatingMenu(
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit,
    onTipClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onRatingListItemClick: (Int) -> Unit,
    expanded: Boolean,
    rating: Int?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0xB3000000), // TODO: place color in the constants file
            ),
        contentAlignment = Alignment.Center
    ){
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ){
            CancelButton {
                onCancelClick()
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                for (i in 10 downTo 1){
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "$i",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        onClick = {
                            onRatingListItemClick(i)
                        },
                        leadingIcon = {
                            if (rating == null || rating < i)
                                Icon(
                                    Icons.Filled.StarBorder,
                                    contentDescription = "Unfilled star at $i",
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            else
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = "Star at $i",
                                    modifier = Modifier.size(18.dp),
                                    tint = PolkaStar
                                )
                        }
                    )
                }
            }

            TipButton {
                onTipClick()
            }
        }
    }
}
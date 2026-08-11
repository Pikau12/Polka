package com.polka.android.presentation.common.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.polka.android.presentation.theme.PolkaButtonAcceptColors
import com.polka.android.presentation.theme.PolkaTheme

@Composable
fun SortCollectionButton(
    modifier: Modifier = Modifier,
){
    var showContextMenu by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Button(
            onClick = {
                showContextMenu = true
            },
            modifier = Modifier
                .width(60.dp)
                .height(40.dp),
            shape = RoundedCornerShape(20.dp),
            colors = PolkaButtonAcceptColors,
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                Icons.Filled.FilterAlt,
                contentDescription = "Sort collection button",
                modifier = Modifier.size(24.dp),
                tint = PolkaButtonAcceptColors.contentColor
            )
        }

        if (showContextMenu){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                DropdownMenu(
                    expanded = showContextMenu,
                    onDismissRequest = { showContextMenu = false },
                    properties = PopupProperties(focusable = true),
                    containerColor = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(0.dp),
                    shadowElevation = 3.dp
                ) {
                    // TODO: add menu items
                }
            }
        }
    }
}

@Preview // TIP: only for preview
@Composable
fun PreviewSortCollectionButton(){
    PolkaTheme {
        SortCollectionButton()
    }
}
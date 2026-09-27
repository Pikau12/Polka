package com.polka.android.presentation.common.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.polka.android.presentation.theme.PolkaAddButtonColors

@Composable
fun AddButton(
    onClick: () -> Unit
) {
    ButtonWithIcon(
        onClick = onClick,
        colors = PolkaAddButtonColors,
        contentDescription = "Add button",
        icon = Icons.Filled.Add
    )
}

@Preview // TIP: only for preview
@Composable
fun ViewAddButton() {
    AddButton{}
}
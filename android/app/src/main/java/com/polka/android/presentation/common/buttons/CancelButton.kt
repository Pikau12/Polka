package com.polka.android.presentation.common.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.polka.android.presentation.theme.PolkaCancelButtonColors

@Composable
fun CancelButton (
    onClick: () -> Unit
){
    ButtonWithIcon(
        onClick = onClick,
        colors = PolkaCancelButtonColors,
        contentDescription = "Cancel button",
        icon = Icons.Filled.Close
    )
}

@Preview // TIP: only for preview
@Composable
fun CancelButtonView() {
    CancelButton {  }
}
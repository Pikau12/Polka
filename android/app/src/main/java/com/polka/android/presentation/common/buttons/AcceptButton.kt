package com.polka.android.presentation.common.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.polka.android.presentation.theme.PolkaAcceptButtonColors

@Composable
fun AcceptButton (
    onClick: () -> Unit
){
    ButtonWithIcon(
        onClick = onClick,
        colors = PolkaAcceptButtonColors,
        contentDescription = "Accept button",
        icon = Icons.Filled.Check
    )
}

@Preview // TIP: only for preview
@Composable
fun AcceptViewButton() {
    AcceptButton({  })
}
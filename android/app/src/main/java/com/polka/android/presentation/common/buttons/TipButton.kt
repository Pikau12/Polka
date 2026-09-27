package com.polka.android.presentation.common.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.polka.android.presentation.theme.PolkaTipButtonColors

@Composable
fun TipButton (
    onClick: () -> Unit
){
    ButtonWithIcon(
        onClick = onClick,
        colors = PolkaTipButtonColors,
        contentDescription = "Tip button",
        icon = Icons.Filled.QuestionMark
    )
}

@Preview(showBackground = true) // TIP: only for preview
@Composable
fun TipButtonView() {
    TipButton {  }
}
package com.polka.android.presentation.common.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AddToPhotos
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.polka.android.presentation.theme.PolkaAcceptButtonColors
import com.polka.android.presentation.theme.PolkaAddSessionButtonColors

@Composable
fun AddSessionButton(
    onClick: () -> Unit
) {
    ButtonWithIcon(
        onClick = onClick,
        colors = PolkaAddSessionButtonColors,
        contentDescription = "Add session button",
        icon = Icons.Outlined.AddToPhotos
    )
}

@Preview // TIP: only for preview
@Composable
fun ViewAddSessionButton() {
    AddSessionButton {}
}

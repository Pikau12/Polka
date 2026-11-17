package com.polka.android.presentation.common.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AddToPhotos
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.polka.android.presentation.theme.PolkaAcceptButtonColors
import com.polka.android.presentation.theme.PolkaAddToCollectionButton
import com.polka.android.presentation.theme.PolkaAddToCollectionButtonColors

@Composable
fun AddToCollectionButton (
    onClick: () -> Unit
){
    ButtonWithIcon(
        onClick = onClick,
        colors = PolkaAddToCollectionButtonColors,
        contentDescription = "Add to collection button",
        icon = Icons.Outlined.AddToPhotos
    )
}

@Preview // TIP: only for preview
@Composable
fun AddToCollectionButtonView() {
    AddToCollectionButton({  })
}
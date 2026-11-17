package com.polka.android.presentation.common.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AddToPhotos
import androidx.compose.material.icons.outlined.Create
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.polka.android.presentation.theme.PolkaAcceptButtonColors
import com.polka.android.presentation.theme.PolkaAddToCollectionButton
import com.polka.android.presentation.theme.PolkaAddToCollectionButtonColors
import com.polka.android.presentation.theme.PolkaEditButton
import com.polka.android.presentation.theme.PolkaEditButtonColors

@Composable
fun EditButton (
    onClick: () -> Unit
){
    ButtonWithIcon(
        onClick = onClick,
        colors = PolkaEditButtonColors,
        contentDescription = "Edit button",
        icon = Icons.Outlined.Create
    )
}

@Preview // TIP: only for preview
@Composable
fun EditButtonView() {
    EditButton({  })
}
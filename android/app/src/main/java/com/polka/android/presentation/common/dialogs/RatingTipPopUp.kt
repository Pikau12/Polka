package com.polka.android.presentation.common.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RatingTipPopUp (
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    TipPopUp(
        modifier = modifier,
        onDismiss = onDismiss,
        message = "Some information about rating" // TODO: change!!!
    )
}
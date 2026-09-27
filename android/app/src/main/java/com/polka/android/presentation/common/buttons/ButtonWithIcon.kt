package com.polka.android.presentation.common.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.polka.android.presentation.common.UiConstants.BUTTON_CORNER_RADIUS
import com.polka.android.presentation.common.UiConstants.BUTTON_ICON_SIZE
import com.polka.android.presentation.common.UiConstants.BUTTON_SIZE

@Composable
fun ButtonWithIcon(
    onClick: () -> Unit,
    colors: ButtonColors,
    contentDescription: String,
    icon: ImageVector
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(BUTTON_SIZE),
        shape = RoundedCornerShape(BUTTON_CORNER_RADIUS),
        colors = colors,
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(BUTTON_ICON_SIZE),
        )
    }
}

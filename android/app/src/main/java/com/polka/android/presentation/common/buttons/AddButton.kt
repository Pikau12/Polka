package com.polka.android.presentation.common.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.polka.android.R
import com.polka.android.presentation.common.UiConstants.BUTTON_CORNER_RADIUS
import com.polka.android.presentation.common.UiConstants.BUTTON_SIZE
import com.polka.android.presentation.theme.PolkaAddButton
import com.polka.android.presentation.theme.PolkaAddButtonColors
import com.polka.android.presentation.theme.PolkaBackButtonColors

@Composable
fun AddButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(BUTTON_SIZE),
        shape = RoundedCornerShape(BUTTON_CORNER_RADIUS),
        colors = PolkaAddButtonColors,
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.add_icon),
            contentDescription = "Add button",
            modifier = Modifier.size(24.dp),
            tint = PolkaAddButtonColors.contentColor
        )
    }
}

@Preview // TIP: only for preview
@Composable
fun ViewAddButton() {
    AddButton{}
}
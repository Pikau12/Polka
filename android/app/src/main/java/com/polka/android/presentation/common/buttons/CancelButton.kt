package com.polka.android.presentation.common.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.polka.android.presentation.theme.PolkaCancelButtonColors

@Composable
fun CancelButton (
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(40.dp),
        shape = RoundedCornerShape(20.dp),
        colors = PolkaCancelButtonColors,
        contentPadding = PaddingValues(0.dp)
    ){
        Icon(
            Icons.Filled.Close,
            contentDescription = "Cancel button",
            modifier = Modifier.size(24.dp),
            tint = PolkaCancelButtonColors.contentColor
        )
    }
}

@Preview // TIP: only for preview
@Composable
fun CancelButtonView() {
    CancelButton {  }
}
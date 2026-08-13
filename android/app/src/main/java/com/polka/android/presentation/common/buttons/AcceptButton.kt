package com.polka.android.presentation.common.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.polka.android.presentation.theme.PolkaAcceptButtonColors

@Composable
fun AcceptButton (
    onClick: () -> Unit
){
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(40.dp),
        shape = RoundedCornerShape(20.dp),
        colors = PolkaAcceptButtonColors,
        contentPadding = PaddingValues(0.dp)
    ){
        Icon(
            Icons.Filled.Check,
            contentDescription = "Accept button",
            modifier = Modifier.size(24.dp),
            tint = PolkaAcceptButtonColors.contentColor
        )
    }
}

@Preview // TIP: only for preview
@Composable
fun AcceptViewButton() {
    val navController = rememberNavController()
    AcceptButton({  })
}
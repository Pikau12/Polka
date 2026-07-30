package com.polka.android.presentation.common.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun BackButton(
    navController: NavController
) {
    Button(
        onClick = {
            if (navController.previousBackStackEntry != null){
                navController.popBackStack()
            }
        },
        modifier = Modifier
            .size(40.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(), // TODO: change colors
        contentPadding = PaddingValues(0.dp)
    ){
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back button",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.inverseOnSurface // TODO: change colors
        )
    }
}

@Preview
@Composable
fun ButtonView() {
    val navController = rememberNavController()
    BackButton(navController = navController)
}
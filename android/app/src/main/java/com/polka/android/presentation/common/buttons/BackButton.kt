package com.polka.android.presentation.common.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.polka.android.presentation.theme.PolkaBackButtonColors

@Composable
fun BackButton(
    navController: NavController
) {
    ButtonWithIcon(
        onClick = {
            if (navController.previousBackStackEntry != null){
                navController.popBackStack()
            }
        },
        colors = PolkaBackButtonColors,
        contentDescription = "Back button",
        icon = Icons.AutoMirrored.Filled.ArrowBack
    )
}

@Preview // TIP: only for preview
@Composable
fun ViewButton() {
    val navController = rememberNavController()
    BackButton(navController = navController)
}
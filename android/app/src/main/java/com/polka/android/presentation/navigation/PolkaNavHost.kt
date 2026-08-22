package com.polka.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun PolkaNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        //startDestination = Destination.Login.route
        startDestination = "core"
    ) {
        //authNavGraph(navController)
        coreAndOtherNavGraph(navController)
    }
}
package com.polka.android.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.polka.android.presentation.authSys.LoginScreen
import com.polka.android.presentation.authSys.OverviewScreen
import com.polka.android.presentation.authSys.SignUpScreen

fun NavGraphBuilder.authNavGraph (
    navController: NavController
) {
    navigation(
        startDestination = Destination.Login.route,
        route = "auth"
    ){
        composable(
            route = Destination.Login.route,
            arguments = listOf(
                navArgument("showSignUpSuccess") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) {
            LoginScreen(navController = navController)
        }

        composable(Destination.SignUp.route){
            SignUpScreen(navController = navController)
        }

        composable(Destination.Overview.route){
            OverviewScreen(
                onSkipClick = {
                    navController.navigate(Destination.CollectionCore.route){
                        popUpTo(Destination.Overview.route) { inclusive = true }
                    }
                },
                onGoToPolkaClick = {
                    navController.navigate(Destination.CollectionCore.route){
                        popUpTo(Destination.Overview.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
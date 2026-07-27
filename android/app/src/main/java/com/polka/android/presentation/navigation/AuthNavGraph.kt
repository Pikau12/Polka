package com.polka.android.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.polka.android.presentation.authSys.LoginScreen
import com.polka.android.presentation.authSys.OverviewScreen
import com.polka.android.presentation.authSys.SigninScreen

fun NavGraphBuilder.authNavGraph (
    navController: NavHostController
) {
    navigation(
        startDestination = Destination.Login.route,
        route = "auth"
    ){
        composable(
            route = Destination.Login.route,
            arguments = listOf(
                navArgument("showSigninSuccess"){ defaultValue = false }
            )
        ){ backStackEntry ->
            val showSuccess = backStackEntry.arguments?.getBoolean("showSigninSuccess") ?: false

            LoginScreen(
                showSigninSuccess = showSuccess,
                onSigninClick = {
                    navController.navigate(Destination.Signin.route)
                },
                onSuccess = { userId ->
                    navController.navigate(
                        Destination.getNextDestinationAfterLogin(userId)
                    ){
                        popUpTo(Destination.Login.route){ inclusive = true }
                    }
                }
            )
        }

        composable(Destination.Signin.route){
            SigninScreen(
                onLoginClick = {
                    navController.navigate(Destination.Login.pass(showSigninSuccess = false)){
                        popUpTo(Destination.Signin.route){ inclusive = true }
                    }
                },
                onSigninSuccess = {
                    navController.navigate(Destination.Login.pass(showSigninSuccess = true)){
                        popUpTo(Destination.Signin.route){ inclusive = true }
                    }
                }
            )
        }

        composable(Destination.Overview.route){
            OverviewScreen(
                onSkipClick = {
                    navController.navigate(Destination.Collection.route){
                        popUpTo(Destination.Overview.route) { inclusive = true }
                    }
                },
                onGoToPolkaClick = {
                    navController.navigate(Destination.Collection.route){
                        popUpTo(Destination.Overview.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
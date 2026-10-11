package com.polka.android.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.polka.android.presentation.coreScreens.CoreCarousel

fun NavGraphBuilder.coreAndOtherNavGraph(
    navController: NavController
){
    navigation(
        startDestination = Destination.Core.route,
        route = "core"
    ){
        composable(
            route = Destination.Core.route,
            arguments = listOf(
                navArgument("startPage") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val startPage = backStackEntry.arguments?.getInt("startPage") ?: 1
            CoreCarousel(
                navController = navController,
                initialPage = startPage
            )
        }

//        composable(Destination.User.route){
//            UserScreen(
//                onToLeftSwipe = {
//                    navController.navigate(Destination.User.route){
//                        popUpTo(Destination.SessionsCore.route){ inclusive = true}
//                    }
//                },
//                onToRightSwipe = {
//                    navController.navigate(Destination.CollectionCore.route){
//                        popUpTo(Destination.SessionsCore.route){ inclusive = true}
//                    }
//                },
//                onSettingsClick = {
//                    navController.navigate(Destination.Settings.route)
//                },
//                onAccountClick = {
//                    navController.navigate(Destination.Account.route)
//                },
//                onSupportProjectClick = {
//                    navController.navigate(Destination.SupportProject.route)
//                },
//                onFriendsClick = {
//                    navController.navigate(Destination.Friends.route)
//                },
//                onRecommendationsClick = {
//                    navController.navigate(Destination.Recommendations.route)
//                },
//                onOverviewClick = {
//                    navController.navigate(Destination.Overview.route)
//                },
//                onLogOutclick = {
//                    navController.navigate(Destination.Login.route){
//                        popUpTo(Destination.User.route){ inclusive = true }
//                    }
//                }
//            ) // TODO: change
//        }

        composable(Destination.Settings.route){

        } // TODO: change

        composable(Destination.Account.route){

        } // TODO: change

        composable(Destination.SupportProject.route){

        } // TODO: change
    }
}
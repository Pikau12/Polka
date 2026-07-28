package com.polka.android.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.polka.android.presentation.coreScreens.CollectionScreen
import com.polka.android.presentation.coreScreens.SessionsScreen
import com.polka.android.presentation.coreScreens.UserScreen

fun NavGraphBuilder.coreAndOtherNavGraph(
    navController: NavController
){
    navigation(
        startDestination = Destination.CollectionCore.route,
        route = "core"
    ){
        composable(Destination.CollectionCore.route){
            CollectionScreen(
                onToLeftSwipe = {
                    navController.navigate(Destination.SessionsCore.route)
                },
                onToRightSwipe = {
                    navController.navigate(Destination.User.route)
                },
                onGamesSearchClick = {
                    navController.navigate(Destination.GamesSearch.route)
                },
                onGameClick = { gameId ->
                    navController.navigate(Destination.Game.pass(gameId))
                },
                onAddGameClick = {
                    navController.navigate(Destination.GameCard.route)
                }
            )
        }

        composable(Destination.SessionsCore.route){
            SessionsScreen(
                onToLeftSwipe = {
                    navController.navigate(Destination.User.route){
                        popUpTo(Destination.SessionsCore.route){ inclusive = true}
                    }
                },
                onToRightSwipe = {
                    navController.navigate(Destination.CollectionCore.route){
                        popUpTo(Destination.SessionsCore.route){ inclusive = true}
                    }
                },
                onSearchClick = {
                    navController.navigate(Destination.SessionsSearch.route)
                },
                onSessionClick = { sessionId ->
                    navController.navigate(Destination.Session.pass(sessionId))
                },
                onAddSessionClick = {
                    navController.navigate(Destination.SessionCard.route)
                }
            )
        }

        composable(Destination.User.route){
            UserScreen(
                onToLeftSwipe = {
                    navController.navigate(Destination.User.route){
                        popUpTo(Destination.SessionsCore.route){ inclusive = true}
                    }
                },
                onToRightSwipe = {
                    navController.navigate(Destination.CollectionCore.route){
                        popUpTo(Destination.SessionsCore.route){ inclusive = true}
                    }
                },
                onSettingsClick = {
                    navController.navigate(Destination.Settings.route)
                },
                onAccountClick = {
                    navController.navigate(Destination.Account.route)
                },
                onSupportProjectClick = {
                    navController.navigate(Destination.SupportProject.route)
                },
                onFriendsClick = {
                    navController.navigate(Destination.Friends.route)
                },
                onRecommendationsClick = {
                    navController.navigate(Destination.Recommendations.route)
                },
                onOverviewClick = {
                    navController.navigate(Destination.Overview.route)
                },
                onLogOutclick = {
                    navController.navigate(Destination.Login.route){
                        popUpTo(Destination.User.route){ inclusive = true }
                    }
                }
            )
        }

        composable(Destination.Settings.route){

        }

        composable(Destination.Account.route){

        }

        composable(Destination.SupportProject.route){

        }
    }
}
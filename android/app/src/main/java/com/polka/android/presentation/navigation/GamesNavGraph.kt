package com.polka.android.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.polka.android.presentation.coreScreens.CollectionScreen
import com.polka.android.presentation.coreScreens.SessionsScreen
import com.polka.android.presentation.gamesSys.GameCardScreen
import com.polka.android.presentation.gamesSys.GameScreen
import com.polka.android.presentation.gamesSys.GamesSearchScreen

fun NavGraphBuilder.gamesNavGraph(
    navController: NavController
){
    navigation(
        startDestination = Destination.CollectionGames.route,
        route = "core"
    ) {
        composable(Destination.CollectionGames.route) {
            CollectionScreen(navController = navController)
        }

        composable(Destination.Game.route) {
            GameScreen(navController = navController)
        }

        composable(Destination.GameCard.route) {
            GameCardScreen(navController = navController)
        }

        composable(Destination.GamesSearch.route) {
            GamesSearchScreen(navController = navController)
        }
    }
}
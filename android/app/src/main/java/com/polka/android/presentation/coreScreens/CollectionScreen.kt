package com.polka.android.presentation.coreScreens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.polka.android.presentation.navigation.Destination

@Composable
fun CollectionScreen(
    navController: NavController,
    viewModel: CollectionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()



    LaunchedEffect(Unit) {
        viewModel.collectionScreenEvent.collect { event ->
            when(event){
                is CollectionScreenEvent.onAddGameClick -> {
                    navController.navigate(Destination.GameCard.route)
                }
                is CollectionScreenEvent.onGameTileClick -> {
                    navController.navigate(Destination.SessionsCore.route)
                }
                is CollectionScreenEvent.onLeftSwipe -> {
                    navController.navigate(Destination.SessionsCore.route)
                }
                is CollectionScreenEvent.onRightSwipe -> {
                    navController.navigate(Destination.User.route)
                }
                else -> {}
                // TODO: add navigation to GameSearchScreen
            }
        }
    }
}
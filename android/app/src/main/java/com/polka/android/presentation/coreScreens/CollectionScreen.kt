package com.polka.android.presentation.coreScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.polka.android.presentation.common.layout.VerticalReorderGrid
import com.polka.android.presentation.common.tiles.ContextMenuAction
import com.polka.android.presentation.navigation.Destination

@Composable
fun CollectionScreen(
    navController: NavController,
    viewModel: CollectionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(

    ) { paddingValues ->
        if (!state.isLoading) {
            VerticalReorderGrid(
                modifier = Modifier.padding(paddingValues),
                collection = state.collection!!,
                onDoubleClick = { gameId ->
                    viewModel.handleEvent(CollectionScreenEvent.onGameTileClick(gameId))
                },
                onContextMenu = { id, action ->
                    when(action){
                        is ContextMenuAction.onAddSessionClick -> {
                            viewModel.handleEvent(CollectionScreenEvent.onAddSessionClick(id.gameId))
                        }
                        is ContextMenuAction.onRatingClick -> {
                            viewModel.handleEvent(CollectionScreenEvent.onRatingMenuOpen(id))
                        }
                        is ContextMenuAction.onStatusClick -> {
                            viewModel.handleEvent(CollectionScreenEvent.onGameStatusClick(id))
                        }
                    }
                },
                onAddGameClick = {
                    viewModel.handleEvent(CollectionScreenEvent.onAddGameClick)
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.collectionScreenEvent.collect { event ->
            when(event){
                is CollectionScreenEvent.onAddGameClick -> {
                    navController.navigate(Destination.GameCard.route)
                }
                is CollectionScreenEvent.onGameTileClick -> {
                    navController.navigate(Destination.Game.pass(event.gameId))
                }
                is CollectionScreenEvent.onLeftSwipe -> {
                    navController.navigate(Destination.SessionsCore.route)
                }
                is CollectionScreenEvent.onRightSwipe -> {
                    navController.navigate(Destination.User.route)
                }
                is CollectionScreenEvent.onAddSessionClick -> {
                    navController.navigate(Destination.SessionCard.pass(event.gameId))
                }
                else -> {}
                // TODO: add navigation to GameSearchScreen
            }
        }
    }
}
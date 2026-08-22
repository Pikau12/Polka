package com.polka.android.presentation.coreScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
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
import com.polka.android.presentation.common.dialogs.TipPopUp
import com.polka.android.presentation.common.layout.VerticalReorderGrid
import com.polka.android.presentation.common.menus.GameRatingMenu
import com.polka.android.presentation.common.menus.GameStatusMenu
import com.polka.android.presentation.common.tiles.ContextMenuAction
import com.polka.android.presentation.navigation.Destination
import kotlin.math.exp

@Composable
fun CollectionScreen(
    navController: NavController,
    viewModel: CollectionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Box {


        Scaffold { paddingValues ->
            if (!state.isLoading && state.collection != null) {
                VerticalReorderGrid(
                    modifier = Modifier.padding(paddingValues),
                    collection = state.collection!!,
                    onDoubleClick = { gameId ->
                        viewModel.handleEvent(CollectionScreenEvent.onGameTileClick(gameId))
                    },
                    onContextMenu = { id, action ->
                        when (action) {
                            is ContextMenuAction.onAddSessionClick -> {
                                viewModel.handleEvent(CollectionScreenEvent.onAddSessionClick(id.gameId))
                            }

                            is ContextMenuAction.onRatingClick -> {
                                viewModel.handleEvent(CollectionScreenEvent.onRatingMenuOpen(id))
                            }

                            is ContextMenuAction.onStatusClick -> {
                                viewModel.handleEvent(CollectionScreenEvent.onStatusMenuOpen(id))
                            }
                        }
                    },
                    onAddGameClick = {
                        viewModel.handleEvent(CollectionScreenEvent.onAddGameClick)
                    }
                )
            }
        }

        if (state.isRatingMenuOpen) {
            GameRatingMenu (
                expanded = state.isRatingMenuOpen,
                onCancelClick = {
                    viewModel.handleEvent(CollectionScreenEvent.onRatingMenuCancel)
                },
                onTipClick = {
                    viewModel.handleEvent(CollectionScreenEvent.onRatingTipClick(
                        message = "Information about rating" // TODO: change!!!!
                    ))
                },
                onDismissRequest = {
                    viewModel.handleEvent(CollectionScreenEvent.onRatingMenuClose)
                },
                onRatingListItemClick = { rating ->
                    viewModel.handleEvent(CollectionScreenEvent.onRatingChange(rating))
                },
                rating = state.collection
                    ?.find { it.id == state.selectedGameId }
                    ?.rating
                    ?: error("Rating not found for game ${state.selectedGameId}")
            )

            if (state.isTipVisible) {
                TipPopUp(
                    message = state.tipMessage
                        ?: error("Tip message is null"),
                    onDismiss = {
                        viewModel.handleEvent(CollectionScreenEvent.onRatingTipClose)
                    }
                )
            }
        }

        if (state.isStatusMenuOpen) {
            GameStatusMenu(
                state.isStatusMenuOpen,
                onDismissRequest = {
                    viewModel.handleEvent(CollectionScreenEvent.onStatusMenuClose)
                },
                onGameStatusItemClick = { status ->
                    viewModel.handleEvent(CollectionScreenEvent.onStatusMenuItemClick(status))
                },
                choicedItems = state.draftStatus!!
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
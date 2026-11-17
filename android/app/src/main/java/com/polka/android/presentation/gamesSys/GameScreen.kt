package com.polka.android.presentation.gamesSys

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.polka.android.presentation.common.buttons.AddSessionButton
import com.polka.android.presentation.common.dialogs.PhotoViewer
import com.polka.android.presentation.common.layout.GameDescriptionBlock
import com.polka.android.presentation.common.layout.GamePhotoBlock
import com.polka.android.presentation.common.layout.GameRatingBlock
import com.polka.android.presentation.common.layout.MainGameInfoBlock
import com.polka.android.presentation.common.layout.TopBar
import sh.calvin.reorderable.rememberReorderableLazyGridState

@Composable
fun GameScreen (
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsState()

    state.game?.let { game ->
        Scaffold(
            topBar = {
                TopBar(
                    text = game.name,
                    navController = navController
                ) {
                    if (!game.isInUserCollection) {
                        // AddToCollectionButton
                    }
                    else {
                        AddSessionButton {
                            viewModel.handleEvent(GameScreenEvent.onAddSessionClick)
                        }
                    }

                    if (game.isActiveUserCreation) {
                        //EditButton
                    }
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues)
            ) {
                item {
                    MainGameInfoBlock(
                        image = game.gameImage,
                        releaseYear = game.releaseYear,
                        playerCount = game.playerCount,
                        bestPlayerCount = game.bestPlayerCount,
                        timeRange = game.timeRange,
                        averageSessionTime = game.averageSessionTime,
                        ageRestriction = game.ageRestriction,
                        weight = game.weight,
                        weightColor = game.weightColor,
                    )
                }

                if (game.shortDescription != null && game.description != null) {
                    item {
                        GameDescriptionBlock(
                            state.isInDetailDescription,
                            onClick = {
                                viewModel.handleEvent(GameScreenEvent.onDescriptionClick)
                            },
                            shortDescription = game.shortDescription,
                            fullDescription = game.description
                        )
                    }
                }

                if (!game.isUserCreated) {
                    if (game.bggAverageRating != null &&
                        game.polkaAverageRating != null &&
                        game.bggAverageRatingColor != null &&
                        game.polkaAverageRatingColor != null &&
                        game.bggNumberOfRatings != null &&
                        game.polkaNumberOfRatings != null) {
                        item {
                            GameRatingBlock(
                                bggAverageRating = game.bggAverageRating,
                                bggAverageRatingColor = game.bggAverageRatingColor,
                                polkaAverageRating = game.polkaAverageRating,
                                polkaAverageRatingColor = game.polkaAverageRatingColor,
                                bggNumberOfRatings = game.bggNumberOfRatings,
                                polkaNumberOfRatings = game.polkaNumberOfRatings,
                                userRating = game.userRating,
                                userRatingColor = game.userRatingColor
                            )
                        }
                    }
                    else {
                        Log.e("Game screen error", "one of the rating fields of game is null when game is not user created")
                    }
                }

                if (game.isInUserCollection) {
                    item {
                        GamePhotoBlock(
                            images = game.userImages,
                            onAddPhotoClick = {

                            },
                            onPhotoClick = {

                            }
                        )
                    }
                }
            }
        }

        state.selectedPhotoIndex?.let { selectedPhotoIndex ->
            PhotoViewer(
                images = game.userImages,
                initialIndex = selectedPhotoIndex,
                onDismiss = {
                    viewModel.handleEvent(GameScreenEvent.onPhotoViewerDismiss)
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.gameScreenEvent.collect {
            when(it){
                else -> {} // TODO : add navigation
            }
        }
    }
}
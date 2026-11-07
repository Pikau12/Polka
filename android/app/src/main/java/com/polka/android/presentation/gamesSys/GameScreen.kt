package com.polka.android.presentation.gamesSys

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.polka.android.presentation.common.dialogs.PhotoViewer

@Composable
fun GameScreen (
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsState()

    state.game?.let { game ->


        state.selectedPhotoIndex?.let { selectedPhotoIndex ->
            PhotoViewer( // нужно будет проверить isInUserCollection == true при вызове GamePhotoBlock
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
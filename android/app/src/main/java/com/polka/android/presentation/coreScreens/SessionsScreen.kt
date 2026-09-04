package com.polka.android.presentation.coreScreens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.polka.android.R
import com.polka.android.data.model.Session
import com.polka.android.presentation.common.layout.SessionSearchGameLayout
import com.polka.android.presentation.navigation.Destination

@Composable
fun SessionsScreen (
    navController: NavController,
    viewModel: SessionsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    if (state.sessions != null && !state.isSearchGameOpen) {

    }
    else if (state.isSearchGameOpen) { // TODO: add collection == null handler
        SessionSearchGameLayout (
            enabled = state.isSearchGameOpen,
            query = state.gameSearchQuery,
            onBackClick = {
                viewModel.handleEvent(SessionsScreenEvent.onGameSearchBackClick)
            },
            onSearchChange = { query ->
                viewModel.handleEvent(SessionsScreenEvent.onGameSearchChange(query))
            },
            backgroundImageId = R.drawable.collection_screen_background, // TODO : change
            placeholderText = "Search game from your collection",
            collection = state.collectionList?: error("Collection in GameSearch on SessionsScreen is not found")
        )
    }

    LaunchedEffect(Unit) { // TODO : update
        viewModel.sessionsScreenEvent.collect { event ->
            when(event) {
                is SessionsScreenEvent.onSessionClick -> {
                    navController.navigate(Destination.Session.pass(event.sessionId))
                }
                is SessionsScreenEvent.onRightSwipe -> {
                    navController.navigate(Destination.CollectionCore.route)
                }
                is SessionsScreenEvent.onLeftSwipe -> {
                    navController.navigate(Destination.User.route)
                }
                else -> {}
            }
        }
    }
}
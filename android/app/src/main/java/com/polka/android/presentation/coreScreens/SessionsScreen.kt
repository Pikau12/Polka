package com.polka.android.presentation.coreScreens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.polka.android.data.model.Session
import com.polka.android.presentation.navigation.Destination

@Composable
fun SessionsScreen (
    navController: NavController,
    viewModel: SessionsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
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
                is SessionsScreenEvent.onAddSessionClick -> {
                    navController.navigate(Destination.SessionCard.pass(event.gameId))
                }
                else -> {}
            }
        }
    }
}
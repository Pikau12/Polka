package com.polka.android.presentation.coreScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.polka.android.R
import com.polka.android.presentation.common.layout.SessionSearchGameLayout
import com.polka.android.presentation.common.layout.SessionsLayout
import com.polka.android.presentation.navigation.Destination

@Composable
fun SessionsScreen (
    navController: NavController,
    viewModel: SessionsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()


    Box {
        Image(
            painter = painterResource(R.drawable.sessions_screen_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error!!,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

            }
            state.sessions != null && !state.isGameSearchOpen -> {
                Scaffold(
                    containerColor = Color.Transparent
                ) { paddingValues ->
                    SessionsLayout(
                        sessions = state.sessions!!,
                        onAddSessionClick = {
                            viewModel.handleEvent(SessionsScreenEvent.onAddSessionClick)
                        },
                        onSessionClick = { sessionId ->
                            viewModel.handleEvent(SessionsScreenEvent.onSessionClick(sessionId))
                        },
                        paddingValues = paddingValues
                    )
                }
            }
            state.isGameSearchOpen -> {
                SessionSearchGameLayout (
                    enabled = true,
                    query = state.gameSearchQuery,
                    onBackClick = {
                        viewModel.handleEvent(SessionsScreenEvent.onGameSearchBackClick)
                    },
                    onSearchChange = { query ->
                        viewModel.handleEvent(SessionsScreenEvent.onGameSearchChange(query))
                    },
                    onSearchItemClick = { id ->
                        viewModel.handleEvent(SessionsScreenEvent.onGameSearchItemClick(id))
                    },
                    placeholderText = "Search game from your collection",
                    collection = state.collectionList?: emptyList()
                )
            }
        }
    }

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
                is SessionsScreenEvent.onGameSearchItemClick -> {
                    navController.navigate(Destination.SessionCard.pass(event.id))
                }
                else -> {}
            }
        }
    }
}
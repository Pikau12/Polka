package com.polka.android.presentation.coreScreens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SessionsScreen (
    navController: NavController,
    viewModel: SessionsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()


}
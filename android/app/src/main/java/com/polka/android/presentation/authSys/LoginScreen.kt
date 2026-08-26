package com.polka.android.presentation.authSys

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.polka.android.R
import com.polka.android.presentation.common.inputs.StringInput
import com.polka.android.presentation.navigation.Destination
import com.polka.android.presentation.theme.PolkaLogInButton
import com.polka.android.presentation.theme.PolkaLogInButtonColors
import com.polka.android.presentation.theme.PolkaOnButton

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // TODO: add enter handler

    // TODO: check body of screen
    Box {
        Image(
            painter = painterResource(R.drawable.collection_screen_background), // TODO: change image
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            containerColor = Color.Transparent
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StringInput(
                        label = "Login",
                        placeholder = "Enter your login*",
                        onValueChange = { login ->
                            viewModel.handleEvent(LoginScreenEvent.onLoginChange(login))
                        },
                        value = state.loginString
                    )

                    StringInput(
                        label = "Password",
                        placeholder = "Enter password of account*",
                        onValueChange = { password ->
                            viewModel.handleEvent(LoginScreenEvent.onPasswordChange(password))
                        },
                        value = state.passwordString
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button (
                        modifier = Modifier
                            .height(94.dp)
                            .height(40.dp),
                        onClick = {
                            viewModel.handleEvent(LoginScreenEvent.onLogInClick)
                        },
                        colors = PolkaLogInButtonColors,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Log in",
                            color = PolkaOnButton
                        )
                    }

                    // TODO: add text to navigate to sign up screen
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loginScreenEvent.collect { event ->
            when (event) {
                is LoginScreenEvent.onToSignUpScreenNav -> {
                    TODO()
                }
                is LoginScreenEvent.onToCollectionScreenNav -> {
                    navController.navigate(Destination.CollectionCore.route) {
                        popUpTo(Destination.Login.route) { inclusive = true }
                    }
                }
                is LoginScreenEvent.onToOverviewScreenNav -> {
                    navController.navigate(Destination.Overview.route) {
                        popUpTo(Destination.Login.route) { inclusive = true }
                    }
                }
                else -> { }
            }
        }
    }
}
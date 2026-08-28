package com.polka.android.presentation.authSys

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.polka.android.R
import com.polka.android.presentation.common.dialogs.MessageBanner
import com.polka.android.presentation.common.inputs.StringInput
import com.polka.android.presentation.common.inputs.StringInputLarge
import com.polka.android.presentation.navigation.Destination
import com.polka.android.presentation.theme.PolkaLogInButtonColors
import com.polka.android.presentation.theme.PolkaOnButton

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleEvent(LoginScreenEvent.onScreenStart)
    }

    Box {
        Image(
            painter = painterResource(R.drawable.auth_screens_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            containerColor = Color.Transparent
        ) { paddingValues ->

            MessageBanner (
                textColor = state.bannerTextColor!!,
                message = state.bannerMessage!!,
                isVisible = state.isBannerVisible
            )

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
                    StringInputLarge(
                        label = "Login",
                        placeholder = "Enter your login*",
                        onValueChange = { login ->
                            viewModel.handleEvent(LoginScreenEvent.onLoginChange(login))
                        },
                        value = state.loginString
                    )

                    StringInputLarge(
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
                            color = PolkaOnButton,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Text(
                        text = buildAnnotatedString {
                            append("Don't have an account? ")
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Blue,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Sign up!")
                            }
                        },
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable {
                            viewModel.handleEvent(LoginScreenEvent.onToSignUpScreenNav)
                        }
                    )
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
package com.polka.android.presentation.authSys

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polka.android.data.usecase.signup.RegisterUseCase
import com.polka.android.presentation.common.UiConstants
import com.polka.android.presentation.coreScreens.SessionsScreenEvent
import com.polka.android.presentation.theme.PolkaErrorTextColor
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignUpState (
    val loginString: String = "",
    val usernameString: String = "",
    val passwordString: String = "",
    val emailString: String = "",
    val isBannerVisible: Boolean = false,
    val bannerMessage: String? = null,
    val bannerTextColor: Color? = null,
)

sealed class SignUpScreenEvent {
    data class onToLoginScreen(val showSignUpSuccess: Boolean): SignUpScreenEvent()

    object onSignUpClick: SignUpScreenEvent()
    data class onLoginChange(val login: String): SignUpScreenEvent()
    data class onUsernameChange(val username: String): SignUpScreenEvent()
    data class onPasswordChange(val password: String): SignUpScreenEvent()
    data class onEmailChange(val email: String): SignUpScreenEvent()

    data class showErrorBanner(val message: String): SignUpScreenEvent()
}

class SignUpViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
): ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()

    private val _signUpScreenEvent = MutableSharedFlow<SignUpScreenEvent>()
    val signUpScreenEvent: SharedFlow<SignUpScreenEvent> = _signUpScreenEvent.asSharedFlow()

    private var bannerJob: Job? = null

    fun handleEvent(event: SignUpScreenEvent){
        when(event) {
            is SignUpScreenEvent.onLoginChange -> handleOnLoginChange(event.login)
            is SignUpScreenEvent.onUsernameChange -> handleOnUsernameChange(event.username)
            is SignUpScreenEvent.onPasswordChange -> handleOnPasswordChange(event.password)
            is SignUpScreenEvent.onEmailChange -> handleOnEmailChange(event.email)
            is SignUpScreenEvent.onSignUpClick -> handleOnSignUpClick()

            is SignUpScreenEvent.showErrorBanner -> handleShowErrorBanner(event.message)

            is SignUpScreenEvent.onToLoginScreen -> handleOnToLoginScreen(event.showSignUpSuccess)
        }
    }

    private fun handleOnEmailChange(email: String) {
        _state.update { it.copy(
            emailString = email
        ) }
    }

    private fun handleOnSignUpClick() {
        if (state.value.loginString == "") {
            handleShowErrorBanner("You need to enter your login to sign up your account")
        }
        else if (state.value.passwordString == "") {
            handleShowErrorBanner("You need to enter your login password to sign up your account")
        }
        else if (state.value.usernameString == "") {
            handleShowErrorBanner("You need to enter your account username to sign up")
        }
        else {
            viewModelScope.launch {
                registerUseCase(
                    login = state.value.loginString,
                    username = state.value.usernameString,
                    password = state.value.passwordString,
                    email = state.value.emailString
                ).fold(
                    onSuccess = {
                        _signUpScreenEvent.emit(SignUpScreenEvent.onToLoginScreen(true))
                    },
                    onFailure = { e ->
                        handleShowErrorBanner(e.toString())
                    }
                )
            }
        }
    }

    private fun handleOnLoginChange(login: String) {
        _state.update { it.copy(
            loginString = login
        ) }
    }

    private fun handleOnUsernameChange(username: String) {
        _state.update { it.copy(
            usernameString = username
        ) }
    }

    private fun handleOnPasswordChange(password: String) {
        _state.update { it.copy(
            passwordString = password
        ) }
    }

    private fun handleOnToLoginScreen(showSignUpSuccess: Boolean) {
        viewModelScope.launch {
            _signUpScreenEvent.emit(SignUpScreenEvent.onToLoginScreen(showSignUpSuccess))
        }
    }

    private fun showBanner(message: String, color: Color) {
        bannerJob?.cancel()

        _state.update { it.copy(
            isBannerVisible = true,
            bannerMessage = message,
            bannerTextColor = color
        ) }

        bannerJob = viewModelScope.launch {
            delay(UiConstants.BANNER_DURATION)
            _state.update { it.copy(
                isBannerVisible = false,
                bannerMessage = null,
                bannerTextColor = null,
            ) }
        }
    }

    private fun handleShowErrorBanner(message: String) {
        showBanner(message, PolkaErrorTextColor)
    }
}
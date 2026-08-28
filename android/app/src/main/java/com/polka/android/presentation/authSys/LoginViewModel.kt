package com.polka.android.presentation.authSys

import android.os.Message
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polka.android.data.usecase.login.LoginUseCase
import com.polka.android.data.usecase.login.ShouldShowOnboarding
import com.polka.android.presentation.navigation.Destination
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class LoginState (
    val showSignUpSuccess: Boolean = false,
    val loginString: String = "",
    val passwordString: String = "",
    val isBannerVisible: Boolean = false,
    val bannerMessage: String? = null
)

sealed class LoginScreenEvent {
    object onToOverviewScreenNav: LoginScreenEvent()
    object onToCollectionScreenNav: LoginScreenEvent()
    object onToSignUpScreenNav: LoginScreenEvent()

    object onLogInClick: LoginScreenEvent()
    data class onLoginChange(val login: String): LoginScreenEvent()
    data class onPasswordChange(val password: String): LoginScreenEvent()

    data class showBanner(val message: String): LoginScreenEvent()
}

class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loginUseCase: LoginUseCase,
    private val shouldShowOnboarding: ShouldShowOnboarding,
): ViewModel() {
    private val _state = MutableStateFlow(
        LoginState(
            showSignUpSuccess = savedStateHandle.get<Boolean>("showSignUpSuccess") ?: false
        )
    )
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _loginScreenEvent = MutableSharedFlow<LoginScreenEvent>()
    val loginScreenEvent: SharedFlow<LoginScreenEvent> = _loginScreenEvent.asSharedFlow()

    fun handleEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.onToCollectionScreenNav -> handleToCollectionScreenNav()
            is LoginScreenEvent.onToSignUpScreenNav -> handleToSignUpScreenNav()
            is LoginScreenEvent.onToOverviewScreenNav -> handleToOverviewScreenNav()

            is LoginScreenEvent.onLogInClick -> handleOnLogInClick()
            is LoginScreenEvent.onLoginChange -> handleOnLoginChange(event.login)
            is LoginScreenEvent.onPasswordChange -> handleOnPasswordChange(event.password)

            is LoginScreenEvent.showBanner -> handleShowBanner(event.message)
        }
    }

    private fun handleShowBanner(message: String) {
        _state.update { it.copy(
            isBannerVisible = true,
            bannerMessage = message
        ) }

        viewModelScope.launch {
            delay(3000.milliseconds)
            _state.update { it.copy(
                isBannerVisible = false,
                bannerMessage = null
            ) }
        }
    }

    private fun handleOnLogInClick() {
        viewModelScope.launch {
            try {
                loginUseCase(
                    login = state.value.loginString,
                    password = state.value.passwordString
                )

                if (shouldShowOnboarding()) {
                    _loginScreenEvent.emit(LoginScreenEvent.onToOverviewScreenNav)
                }
                else {
                    _loginScreenEvent.emit(LoginScreenEvent.onToCollectionScreenNav)
                }

            } catch (e: Exception) {
                handleEvent(LoginScreenEvent.showBanner(e.toString())) // TODO: check
            }
        }
    }

    private fun handleOnLoginChange(login: String) {
        _state.update { it.copy(
            loginString = login
        ) }
    }

    private fun handleOnPasswordChange(password: String) {
        _state.update { it.copy(
            passwordString = password
        ) }
    }

    private fun handleToOverviewScreenNav() {
        viewModelScope.launch {
            _loginScreenEvent.emit(LoginScreenEvent.onToOverviewScreenNav)
        }
    }

    private fun handleToCollectionScreenNav() {
        viewModelScope.launch {
            _loginScreenEvent.emit(LoginScreenEvent.onToCollectionScreenNav)
        }
    }

    private fun handleToSignUpScreenNav() {
        viewModelScope.launch {
            _loginScreenEvent.emit(LoginScreenEvent.onToSignUpScreenNav)
        }
    }
}
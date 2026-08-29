package com.polka.android.presentation.authSys

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polka.android.data.usecase.login.LoginUseCase
import com.polka.android.data.usecase.login.ShouldShowLoginScreen
import com.polka.android.data.usecase.login.ShouldShowOnboarding
import com.polka.android.presentation.navigation.Destination
import com.polka.android.presentation.theme.PolkaErrorTextColor
import com.polka.android.presentation.theme.PolkaSuccessTextColor
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
import kotlin.time.Duration.Companion.milliseconds
import com.polka.android.presentation.common.UiConstants

data class LoginState (
    val showSignUpSuccess: Boolean = false,
    val loginString: String = "",
    val passwordString: String = "",
    val isBannerVisible: Boolean = false,
    val bannerMessage: String? = null,
    val bannerTextColor: Color? = null,
)

sealed class LoginScreenEvent {
    object onToOverviewScreenNav: LoginScreenEvent()
    object onToCollectionScreenNav: LoginScreenEvent()
    object onToSignUpScreenNav: LoginScreenEvent()

    object onLogInClick: LoginScreenEvent()
    data class onLoginChange(val login: String): LoginScreenEvent()
    data class onPasswordChange(val password: String): LoginScreenEvent()
    object onScreenStart: LoginScreenEvent()

    data class showErrorBanner(val message: String): LoginScreenEvent()
    object showSignUpSuccess: LoginScreenEvent()
}

class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loginUseCase: LoginUseCase,
    private val shouldShowOnboarding: ShouldShowOnboarding,
    private val shouldShowLoginScreen: ShouldShowLoginScreen,
): ViewModel() {
    private val _state = MutableStateFlow(
        LoginState(
            showSignUpSuccess = savedStateHandle.get<Boolean>("showSignUpSuccess") ?: false
        )
    )
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _loginScreenEvent = MutableSharedFlow<LoginScreenEvent>()
    val loginScreenEvent: SharedFlow<LoginScreenEvent> = _loginScreenEvent.asSharedFlow()

    private var bannerJob: Job? = null

    init {
        if (_state.value.showSignUpSuccess) {
            handleShowSignUpSuccess()
        }
    }

    fun handleEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.onToCollectionScreenNav -> handleToCollectionScreenNav()
            is LoginScreenEvent.onToSignUpScreenNav -> handleToSignUpScreenNav()
            is LoginScreenEvent.onToOverviewScreenNav -> handleToOverviewScreenNav()

            is LoginScreenEvent.onLogInClick -> handleOnLogInClick()
            is LoginScreenEvent.onLoginChange -> handleOnLoginChange(event.login)
            is LoginScreenEvent.onPasswordChange -> handleOnPasswordChange(event.password)
            is LoginScreenEvent.onScreenStart -> handleOnScreenStart()

            is LoginScreenEvent.showErrorBanner -> handleShowErrorBanner(event.message)
            is LoginScreenEvent.showSignUpSuccess -> handleShowSignUpSuccess()
        }
    }

    private fun handleOnScreenStart() {
        if (!shouldShowLoginScreen()) {
            handleToCollectionScreenNav()
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
    private fun handleShowSignUpSuccess() {
        showBanner("Registration was successful", PolkaSuccessTextColor)
    }

    private fun handleShowErrorBanner(message: String) {
        showBanner(message, PolkaErrorTextColor)
    }

    private fun handleOnLogInClick() {
        if (state.value.loginString == "") {
            handleShowErrorBanner("You need to enter your login to log in to your account")
        }
        else if (state.value.passwordString == "") {
            handleShowErrorBanner("You need to enter your login password to log in to your account")
        }
        else {
            viewModelScope.launch {
                try {
                    loginUseCase(
                        login = state.value.loginString,
                        password = state.value.passwordString
                    )

                    if (shouldShowOnboarding()) {
                        _loginScreenEvent.emit(LoginScreenEvent.onToOverviewScreenNav)
                    } else {
                        _loginScreenEvent.emit(LoginScreenEvent.onToCollectionScreenNav)
                    }

                } catch (e: Exception) {
                    handleShowErrorBanner(e.toString()) // TODO: check
                }
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
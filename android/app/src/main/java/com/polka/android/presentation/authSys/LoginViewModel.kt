package com.polka.android.presentation.authSys

data class LoginState (
    val loginString: String = "",
    val passwordString: String = "",
    val message: String? = null,
)

sealed class LoginScreenEvent {
    object onToOverviewScreenNav: LoginScreenEvent()
    object onToCollectionScreenNav: LoginScreenEvent()
    object onToSignUpScreenNav: LoginScreenEvent()

    object onLogInClick: LoginScreenEvent()
    object onLoginChange: LoginScreenEvent()
    object onPasswordChange: LoginScreenEvent()
}

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _loginScreenEvent = MutableSharedFlow<LoginScreenEvent>()
    val loginScreenEvent: SharedFlow<LoginScreenEvent> = _loginScreenEvent.asSharedFlow()

    fun handleEvent(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.onToCollectionScreenNav -> handleToCollectionScreenNav()
            is LoginScreenEvent.onToSignUpScreenNav -> handleToSignUpScreenNav()
            is LoginScreenEvent.onToOverviewScreenNav -> handleToOverviewScreenNav()

            is LoginScreenEvent.onLogInClick -> handleOnLoginChange()
            is LoginScreenEvent.onLoginChange -> handleOnLoginChange()
            is LoginScreenEvent.onPasswordChange -> handleOnPasswordChange()
        }
    }

    private fun handleOnLogInClick() {

    }

    private fun handleOnLoginChange() {

    }

    private fun handleOnPasswordChange() {

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
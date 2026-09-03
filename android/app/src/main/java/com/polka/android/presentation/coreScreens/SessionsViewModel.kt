package com.polka.android.presentation.coreScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polka.android.presentation.model.SessionSummary
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SessionsState (
    val isLoading: Boolean = false,
    val sessions: List<SessionSummary>? = null,
)

sealed class SessionsScreenEvent {
    object onLeftSwipe : SessionsScreenEvent()
    object onRightSwipe : SessionsScreenEvent()
    data class onSessionClick(val sessionId: Long) : SessionsScreenEvent()
    data class onAddSessionClick(val gameId: Long) : SessionsScreenEvent()
}

class SessionsViewModel @Inject constructor(

) : ViewModel() { // TODO add sort
    private val _state = MutableStateFlow(SessionsState())
    val state : StateFlow<SessionsState> = _state.asStateFlow()

    private val _sessionsScreenEvent = MutableSharedFlow<SessionsScreenEvent>()
    val sessionsScreenEvent: SharedFlow<SessionsScreenEvent> = _sessionsScreenEvent.asSharedFlow()

    fun handleEvent(event: SessionsScreenEvent) {
        when(event) {
            is SessionsScreenEvent.onLeftSwipe -> handleOnLeftSwipe()
            is SessionsScreenEvent.onRightSwipe -> handleOnRightSwipe()
            is SessionsScreenEvent.onSessionClick -> handleOnSessionClick(event.sessionId)
            is SessionsScreenEvent.onAddSessionClick -> handleOnAddSessionClick(event.gameId)
        }
    }

    private fun handleOnAddSessionClick(gameId: Long) {
        viewModelScope.launch {
            _sessionsScreenEvent.emit(SessionsScreenEvent.onAddSessionClick(gameId))
        }
    }

    private fun handleOnLeftSwipe() {
        viewModelScope.launch {
            _sessionsScreenEvent.emit(SessionsScreenEvent.onLeftSwipe)
        }
    }

    private fun handleOnRightSwipe() {
        viewModelScope.launch {
            _sessionsScreenEvent.emit(SessionsScreenEvent.onRightSwipe)
        }
    }

    private fun handleOnSessionClick(sessionId: Long) {
        viewModelScope.launch {
            _sessionsScreenEvent.emit(SessionsScreenEvent.onSessionClick(sessionId))
        }
    }
}
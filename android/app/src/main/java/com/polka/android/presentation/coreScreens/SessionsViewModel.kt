package com.polka.android.presentation.coreScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polka.android.presentation.model.CollectionItemSummary
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
    val collectionList: List<CollectionItemSummary>? = null,
    val isSearchGameOpen: Boolean = false,
    val gameSearchQuery: String = "",
)

sealed class SessionsScreenEvent {
    object onLeftSwipe : SessionsScreenEvent()
    object onRightSwipe : SessionsScreenEvent()
    data class onSessionClick(val sessionId: Long) : SessionsScreenEvent()
    object onAddSessionClick : SessionsScreenEvent()
    data class onGameSearchItemClick(val id: CollectionItemSummary.Id) : SessionsScreenEvent()
    object onGameSearchBackClick : SessionsScreenEvent()
    data class onGameSearchChange(val query: String) : SessionsScreenEvent()
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

            is SessionsScreenEvent.onAddSessionClick -> handleOnAddSessionClick()
            is SessionsScreenEvent.onGameSearchItemClick -> TODO()
            is SessionsScreenEvent.onGameSearchBackClick -> handleOnGameSearchBackClick()
        }
    }

    private fun handleOnGameSearchChange() {

    }

    private fun handleOnGameSearchBackClick() {
        // TODO
    }

    private fun handleOnGameSearchItemClick(id: CollectionItemSummary.Id) {
        // TODO
    }

    private fun handleOnAddSessionClick() {
       // TODO
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
package com.polka.android.presentation.coreScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polka.android.data.usecase.sessions.ObserveSessionsUseCase
import com.polka.android.data.usecase.sessions.SearchCollectionSummaryUseCase
import com.polka.android.presentation.model.CollectionItemSummary
import com.polka.android.presentation.model.SessionSummary
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SessionsState(
    val isLoading: Boolean = false,
    val isSearchLoading: Boolean = false,
    val sessions: List<SessionSummary>? = null,
    val collectionList: List<CollectionItemSummary>? = null,
    val isGameSearchOpen: Boolean = false,
    val gameSearchQuery: String = "",
    val error: String? = null,
)

sealed class SessionsScreenEvent {
    object onLeftSwipe : SessionsScreenEvent()
    object onRightSwipe : SessionsScreenEvent()
    data class onSessionClick(val sessionId: Long) : SessionsScreenEvent()
    object onAddSessionClick : SessionsScreenEvent()
    data class onGameSearchItemClick(val id: Long) : SessionsScreenEvent()
    object onGameSearchBackClick : SessionsScreenEvent()
    data class onGameSearchChange(val query: String) : SessionsScreenEvent()
}

class SessionsViewModel @Inject constructor(
    private val searchCollectionSummaryUseCase: SearchCollectionSummaryUseCase,
    private val observeSessionsUseCase: ObserveSessionsUseCase
) : ViewModel() { // TODO add sort
    private val _state = MutableStateFlow(SessionsState())
    val state: StateFlow<SessionsState> = _state.asStateFlow()

    private val _sessionsScreenEvent = MutableSharedFlow<SessionsScreenEvent>()
    val sessionsScreenEvent: SharedFlow<SessionsScreenEvent> = _sessionsScreenEvent.asSharedFlow()

    private var searchJob: Job? = null

    init {
        observeSessions()
        observeCollectionSummary()
    }

    private fun observeSessions() {
        viewModelScope.launch {
            observeSessionsUseCase()
                .onStart { _state.update { it.copy(
                    isLoading = true,
                    error = null
                ) } }
                .catch { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
                .collect { sessions ->
                    _state.update { it.copy(
                        sessions = sessions,
                        isLoading = false
                    ) }
                }
        }
    }

    private fun observeCollectionSummary() {
        viewModelScope.launch {
            searchCollectionSummaryUseCase("") // TIP: "" equal to See all collectionItems
                .onStart { _state.update { it.copy(
                        isSearchLoading = true,
                        error = null
                    ) } }
                .catch { e ->
                    _state.update {
                        it.copy(
                            isSearchLoading = false,
                            error = e.message
                        )
                    }
                }
                .collect { collectionList ->
                    _state.update { it.copy(collectionList = collectionList) }
                }
        }
    }

    fun handleEvent(event: SessionsScreenEvent) {
        when (event) {
            is SessionsScreenEvent.onLeftSwipe -> handleOnLeftSwipe()
            is SessionsScreenEvent.onRightSwipe -> handleOnRightSwipe()
            is SessionsScreenEvent.onSessionClick -> handleOnSessionClick(event.sessionId)

            is SessionsScreenEvent.onAddSessionClick -> handleOnAddSessionClick()
            is SessionsScreenEvent.onGameSearchItemClick -> handleOnGameSearchItemClick(event.id)
            is SessionsScreenEvent.onGameSearchBackClick -> handleOnGameSearchBackClick()
            is SessionsScreenEvent.onGameSearchChange -> handleOnGameSearchChange(event.query)
        }
    }

    private fun handleOnGameSearchChange(query: String) {
        _state.update { it.copy(gameSearchQuery = query) }

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            searchCollectionSummaryUseCase(query)
                .onStart {
                    _state.update { it.copy(
                        isSearchLoading = true,
                        error = null
                    ) }
                }
                .catch { e ->
                    _state.update {
                        it.copy(
                            isSearchLoading = false,
                            error = e.message
                        )
                    }
                }
                .collect { collectionList ->
                    _state.update { it.copy(
                        collectionList = collectionList,
                        isSearchLoading = false
                    ) }
                }
        }
    }

    private fun handleOnGameSearchBackClick() {
        searchJob?.cancel()

        _state.update { it.copy(
            isGameSearchOpen = false,
            gameSearchQuery = ""
        ) }
    }

    private fun handleOnGameSearchItemClick(id: Long) {
        handleOnGameSearchBackClick()

        viewModelScope.launch {
            _sessionsScreenEvent.emit(SessionsScreenEvent.onGameSearchItemClick(id))
        }
    }

    private fun handleOnAddSessionClick() {
        _state.update { it.copy(isGameSearchOpen = true) }

        if (_state.value.collectionList == null) {
            observeCollectionSummary()
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
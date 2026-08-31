package com.polka.android.presentation.coreScreens

import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.polka.android.data.model.Session
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SessionsState (
    val isLoading: Boolean = false,
    val sessions: List<Session>? = null,
)

sealed class SessionsScreenEvent {
    data class onSessionClick(val session)
}

class SessionsViewModel @Inject constructor(

) : ViewModel() {
    private val _state = MutableStateFlow(SessionsState())
    val state : StateFlow<SessionsState> = _state.asStateFlow()
}
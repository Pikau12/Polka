package com.polka.android.presentation.gamesSys

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.polka.android.presentation.coreScreens.CollectionState
import com.polka.android.presentation.model.Game
import com.polka.android.utils.colorOverlay
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GameState(
    val game: Game? = null,
)

sealed class GameScreenEvent {
    object onBackClick: GameScreenEvent()
    object onAddGameToCollectionClick: GameScreenEvent()
    object onAddSessionClick: GameScreenEvent()
}

@HiltViewModel
class GameViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(
        GameState(
            //game =
        )
    )
    val state: StateFlow<GameState> = _state.asStateFlow()

    init {

    }

    fun handleEvent(event: GameScreenEvent) {
        when(event) {
            else -> { }
        }
    }
}
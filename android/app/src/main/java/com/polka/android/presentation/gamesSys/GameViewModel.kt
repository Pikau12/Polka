package com.polka.android.presentation.gamesSys

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Index
import com.polka.android.data.usecase.collection.UpdateGameRatingUseCase
import com.polka.android.data.usecase.user.GetCurrentUserIdUseCase
import com.polka.android.presentation.coreScreens.CollectionScreenEvent
import com.polka.android.presentation.model.CollectionItem
import com.polka.android.presentation.model.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameState(
    val game: Game? = null,
    val selectedPhotoIndex: Int? = null,

    val draftRating: Int? = null,
    val isRatingMenuOpen: Boolean = false,
    val isRatingTipVisible: Boolean = false,
)

sealed class GameScreenEvent {
    object onAddGameToCollectionClick: GameScreenEvent()
    object onAddSessionClick: GameScreenEvent()

    object onUserRatingClick: GameScreenEvent()
    data class onRatingChange(val rating: Int) : GameScreenEvent()
    object onRatingMenuCancel: GameScreenEvent()
    object onRatingMenuClose : GameScreenEvent()
    object onRatingTipClick : GameScreenEvent()
    object onRatingTipClose : GameScreenEvent()

    data class onPhotoClick(val photoIndex: Int) : GameScreenEvent()
    object onPhotoViewerDismiss : GameScreenEvent()
}

@HiltViewModel
class GameViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,
    private val updateGameRatingUseCase: UpdateGameRatingUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(
        GameState(
            //game =
        )
    )
    val state: StateFlow<GameState> = _state.asStateFlow()

    private val _gameScreenEvent = MutableSharedFlow<GameScreenEvent>()
    val gameScreenEvent: SharedFlow<GameScreenEvent> =
        _gameScreenEvent.asSharedFlow()

    init {

    }

    fun handleEvent(event: GameScreenEvent) {
        when(event) {
            GameScreenEvent.onAddGameToCollectionClick -> TODO()
            GameScreenEvent.onAddSessionClick -> TODO()

            is GameScreenEvent.onRatingChange -> handleOnRatingChange(event.rating)
            GameScreenEvent.onRatingMenuCancel -> handleOnRatingMenuCancel()
            GameScreenEvent.onRatingMenuClose -> handleOnRatingMenuClose()
            GameScreenEvent.onRatingTipClick -> handleOnRatingTipClick()
            GameScreenEvent.onRatingTipClose -> handleOnRatingTipClose()
            GameScreenEvent.onUserRatingClick -> handleOnUserRatingClick()

            is GameScreenEvent.onPhotoClick -> handleOnPhotoClick(event.photoIndex)
            GameScreenEvent.onPhotoViewerDismiss -> handleOnPhotoViewerDismiss()
        }
    }

    private fun handleOnPhotoClick(photoIndex: Int){
        _state.update { it.copy(
            selectedPhotoIndex = photoIndex
        ) }
    }

    private fun handleOnPhotoViewerDismiss(){
        _state.update { it.copy(
            selectedPhotoIndex = null
        ) }
    }

    private fun handleOnRatingTipClick(){
        _state.update { it.copy(
            isRatingTipVisible = true
        ) }
    }

    private fun handleOnRatingTipClose(){
        _state.update { it.copy(
            isRatingTipVisible = false
        ) }
    }

    private fun handleOnUserRatingClick(){
        val currentRating = _state.value.game?.userRating

        _state.update { it.copy(
            isRatingMenuOpen = true,
            draftRating = currentRating
        ) }
    }

    private fun handleOnRatingChange(rating: Int){
        _state.update { it.copy(
            draftRating = rating
        ) }
    }

    private fun handleOnRatingMenuCancel(){
        _state.update { it.copy(
            isRatingMenuOpen = false,
            draftRating = null,
        ) }
    }

    private fun handleOnRatingMenuClose() {
        val rating = _state.value.draftRating
        val gameId = _state.value.game?.id

        _state.update { it.copy(
            isRatingMenuOpen = false,
            draftRating = null
        ) }

        if (gameId != null) {
            viewModelScope.launch {
                updateGameRatingUseCase(
                    CollectionItem.Id(getCurrentUserIdUseCase(), gameId),
                    rating
                )
            }
        }
    }
}
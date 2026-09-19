package com.polka.android.presentation.coreScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polka.android.data.model.CollectionSortQuery
import com.polka.android.data.model.isSortQueryEmpty
import com.polka.android.data.usecase.collection.ObserveCollectionUseCase
import com.polka.android.data.usecase.collection.UpdateGameRatingUseCase
import com.polka.android.data.usecase.collection.UpdateGameStatusUseCase
import com.polka.android.presentation.model.CollectionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CollectionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSearchOpen: Boolean = false,
    val isSortMenuOpen: Boolean = false,
    val collection: List<CollectionItem>? = null,
    val searchQuery: String? = null,
    val sortQuery: CollectionSortQuery? = null,
    val draftSortQuery: CollectionSortQuery? = null,
    val draftRating: Int? = null,
    val selectedGameId: CollectionItem.Id? = null,
    val isRatingMenuOpen: Boolean = false,
    val isTipVisible: Boolean = false,
    val tipMessage: String? = null,
    val draftStatus: Set<CollectionItem.Status>? = null,
    val isStatusMenuOpen: Boolean = false,
)

sealed class CollectionScreenEvent {
    object onAddGameClick : CollectionScreenEvent()
    data class onGameTileClick(val gameId: Long) : CollectionScreenEvent()
    object onLeftSwipe : CollectionScreenEvent()
    object onRightSwipe : CollectionScreenEvent()

    // Sort
    object onSortMenuOpen : CollectionScreenEvent()
    object onSortMenuClose : CollectionScreenEvent()
    data class onDraftSortChanged(val query: CollectionSortQuery): CollectionScreenEvent()
    object onSortMenuCancel: CollectionScreenEvent()

    // GameTile context menu
    data class onStatusMenuOpen(val id: CollectionItem.Id) : CollectionScreenEvent()
    data class onStatusMenuItemClick(val status: CollectionItem.Status) : CollectionScreenEvent()
    object onStatusMenuClose: CollectionScreenEvent()
    data class onRatingMenuOpen(val id: CollectionItem.Id): CollectionScreenEvent()
    data class onRatingChange(val rating: Int) : CollectionScreenEvent()
    object onRatingMenuCancel: CollectionScreenEvent()
    object onRatingMenuClose : CollectionScreenEvent()
    data class onAddSessionClick(val gameId: Long) : CollectionScreenEvent()
    data class onRatingTipClick(val message: String) : CollectionScreenEvent()
    object onRatingTipClose: CollectionScreenEvent()
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val observeCollectionUseCase: ObserveCollectionUseCase,
    private val updateGameRatingUseCase: UpdateGameRatingUseCase,
    private val updateGameStatusUseCase: UpdateGameStatusUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(CollectionState())
    val state: StateFlow<CollectionState> = _state.asStateFlow()

    private val _collectionScreenEvent = MutableSharedFlow<CollectionScreenEvent>()
    val collectionScreenEvent: SharedFlow<CollectionScreenEvent> =
        _collectionScreenEvent.asSharedFlow()

    private val _sortQuery = MutableStateFlow<CollectionSortQuery?>(null)

    init {
        _sortQuery
            .flatMapLatest { sort ->
                observeCollectionUseCase(sort)
            }
            .onEach { collection ->
                _state.update { it.copy(
                    collection = collection,
                    isLoading = false
                )}
            }
            .catch { e ->
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    fun handleEvent(event: CollectionScreenEvent) {
        when (event) {
            is CollectionScreenEvent.onAddGameClick -> handleOnAddGameClick()
            is CollectionScreenEvent.onGameTileClick -> handleOnGameTileClick(event.gameId)

            is CollectionScreenEvent.onLeftSwipe -> handleOnLeftSwipe()
            is CollectionScreenEvent.onRightSwipe -> handleOnRightSwipe()

            is CollectionScreenEvent.onSortMenuOpen -> handleOnSortMenuOpen()
            is CollectionScreenEvent.onDraftSortChanged -> handleOnDraftSortChanged(event.query)
            is CollectionScreenEvent.onSortMenuClose -> handleOnSortMenuClose()
            is CollectionScreenEvent.onSortMenuCancel -> handleOnSortMenuCancel()

            is CollectionScreenEvent.onAddSessionClick -> handleOnAddSessionClick(event.gameId)

            is CollectionScreenEvent.onStatusMenuOpen -> handleOnStatusMenuOpen(event.id)
            is CollectionScreenEvent.onStatusMenuItemClick -> handleOnStatusMenuItemClick(event.status)
            is CollectionScreenEvent.onStatusMenuClose -> handleOnStatusMenuClose()

            is CollectionScreenEvent.onRatingChange -> handleOnRatingChange(event.rating)
            is CollectionScreenEvent.onRatingMenuCancel -> handleOnRatingMenuCancel()
            is CollectionScreenEvent.onRatingMenuOpen -> handleOnRatingMenuOpen(event.id)
            is CollectionScreenEvent.onRatingMenuClose -> handleOnRatingMenuClose()
            is CollectionScreenEvent.onRatingTipClick -> handleOnRatingTipClick(event.message)
            is CollectionScreenEvent.onRatingTipClose -> handleOnRatingTipClose()
        }
    }

    private fun handleOnStatusMenuOpen(id: CollectionItem.Id){
        val selectedGame = state.value.collection?.find {
            it.id == state.value.selectedGameId
        }

        _state.update { it.copy(
            isStatusMenuOpen = true,
            selectedGameId = id,
            draftStatus = selectedGame?.status
        ) }
    }

    private fun handleOnStatusMenuItemClick(status: CollectionItem.Status){ // TODO: check
        if (status.toWishlist() == null){
            if (status in state.value.draftStatus!!) {
                _state.update {
                    it.copy(
                        draftStatus = state.value.draftStatus?.minus(status)
                    )
                }
            }
            else {
                _state.update {
                    it.copy(
                        draftStatus = state.value.draftStatus?.plus(status)
                    )
                }
            }
        }
        else {
            if (status in state.value.draftStatus!!) {
                _state.update {
                    it.copy(
                        draftStatus = state.value.draftStatus?.minus(status)
                    )
                }
            }
            else if (CollectionItem.Status.Wishlist.entries.any {
                state.value.draftStatus?.contains(it.toStatus()) == true
            }) {
                var currentStatus = state.value.draftStatus

                for (wishlistStatus in CollectionItem.Status.Wishlist.entries) {
                    currentStatus = currentStatus?.minus(wishlistStatus.toStatus())
                }

                currentStatus?.plus(status)

                _state.update { it.copy(
                    draftStatus = currentStatus
                ) }
            }
            else {
                _state.update { it.copy(
                    draftStatus = state.value.draftStatus?.plus(status)
                ) }
            }
        }
    }

    private fun handleOnStatusMenuClose(){
        val currentStatus = state.value.draftStatus
        val id = state.value.selectedGameId

        _state.update { it.copy(
            draftStatus = null,
            selectedGameId = null,
            isStatusMenuOpen = false,
        ) }

        viewModelScope.launch {
            updateGameStatusUseCase(
                id!!,
                currentStatus!!
            )
        }
    }

    private fun handleOnRatingTipClick(message: String){
        _state.update { it.copy(
            isTipVisible = true,
            tipMessage = message,
        ) }
    }

    private fun handleOnRatingTipClose(){
        _state.update { it.copy(
            isTipVisible = false,
            tipMessage = null,
        ) }
    }

    private fun handleOnAddGameClick() {
        viewModelScope.launch {
            _collectionScreenEvent.emit(CollectionScreenEvent.onAddGameClick)
        }
    }

    private fun handleOnGameTileClick(gameId: Long) {
        viewModelScope.launch {
            _collectionScreenEvent.emit(CollectionScreenEvent.onGameTileClick(gameId))
        }
    }

    private fun handleOnLeftSwipe() {
        viewModelScope.launch {
            _collectionScreenEvent.emit(CollectionScreenEvent.onLeftSwipe)
        }
    }

    private fun handleOnRightSwipe() {
        viewModelScope.launch {
            _collectionScreenEvent.emit(CollectionScreenEvent.onRightSwipe)
        }
    }

    private fun handleOnSortMenuOpen() {
        _state.update { it.copy(
            isSortMenuOpen = true,
            draftSortQuery = it.sortQuery ?: CollectionSortQuery()
        )}
    }

    private fun handleOnDraftSortChanged(query: CollectionSortQuery) {
        _state.update { it.copy(draftSortQuery = query) }
    }

    private fun handleOnSortMenuClose() {
        var draft = _state.value.draftSortQuery
        draft = if (draft == null) null else
            if (isSortQueryEmpty(draft)) null else draft
        _state.update { it.copy(
            sortQuery = draft,
            isSortMenuOpen = false
        )}
        _sortQuery.value = draft
    }

    private fun handleOnSortMenuCancel() {
        _state.update { it.copy(
            isSortMenuOpen = false,
            sortQuery = null,
            draftSortQuery = null,
            ) }
    }

    private fun handleOnAddSessionClick(gameId: Long){
        viewModelScope.launch {
            _collectionScreenEvent.emit(CollectionScreenEvent.onAddSessionClick(gameId))
        }
    }

    private fun handleOnRatingMenuOpen(id: CollectionItem.Id){
        val currentRating = _state.value.collection
            ?.find{ it.id == id}
            ?.rating

        _state.update { it.copy(
            isRatingMenuOpen = true,
            selectedGameId = id,
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
            selectedGameId = null
        ) }
    }

    private fun handleOnRatingMenuClose(){
        val rating = _state.value.draftRating
        val selectedGameId = _state.value.selectedGameId

        _state.update { it.copy(
            isRatingMenuOpen = false,
            draftRating = null,
            selectedGameId = null
        ) }

        viewModelScope.launch {
            updateGameRatingUseCase(selectedGameId!!, rating)
        }
    }
}
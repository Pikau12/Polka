package com.polka.android.presentation.coreScreens

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polka.android.data.model.SortQuery
import com.polka.android.data.model.isSortQueryEmpty
import com.polka.android.presentation.common.tiles.ContextMenuAction
import com.polka.android.presentation.model.CollectionItem
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

data class CollectionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSearchOpen: Boolean = false,
    val collection: List<CollectionItem>? = null,
    val sortedCollection : List<CollectionItem>? = null,
    val searchQuery: String? = null,
    val sortQuery: SortQuery? = null,
    val showSortedCollection: Boolean = false,
)

sealed class CollectionScreenEvent {
    object onAddGameClick : CollectionScreenEvent()
    data class onGameTileClick(val gameId: Long) : CollectionScreenEvent()
    object onLeftSwipe : CollectionScreenEvent()
    object onRightSwipe : CollectionScreenEvent()
    data class changeSortQuery(val query: SortQuery): CollectionScreenEvent()
    object closeSortMenu: CollectionScreenEvent()
    object cancelSort: CollectionScreenEvent()
    // TODO: add more
}

@HiltViewModel
class CollectionViewModel @Inject constructor(

) : ViewModel() {
    private val _state = MutableStateFlow(CollectionState())
    val state: StateFlow<CollectionState> = _state.asStateFlow()

    private val _collectionScreenEvent = MutableSharedFlow<CollectionScreenEvent>()
    val collectionScreenEvent: SharedFlow<CollectionScreenEvent> =
        _collectionScreenEvent.asSharedFlow()

    init {
        // TODO: add loading collection
    }

    fun handleEvent(event: CollectionScreenEvent) {
        when (event) {
            is CollectionScreenEvent.onAddGameClick -> handleOnAddGameClick()
            is CollectionScreenEvent.onGameTileClick -> handleOnGameTileClick(event.gameId)
            is CollectionScreenEvent.onLeftSwipe -> handleOnLeftSwipe()
            is CollectionScreenEvent.onRightSwipe -> handleOnRightSwipe()
            is CollectionScreenEvent.changeSortQuery -> handleChangeSortQuery(event.query)
            is CollectionScreenEvent.closeSortMenu -> handleCloseSortMenu()
            is CollectionScreenEvent.cancelSort -> handleCancelSort()
        }
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

    private fun handleChangeSortQuery(query: SortQuery) {
        _state.update { currentState ->
            currentState.copy(
                sortQuery = if (isSortQueryEmpty(query)) null else query,

                collection = currentState.collection,
                sortedCollection = currentState.sortedCollection,
                showSortedCollection = currentState.showSortedCollection
            )
        }
    }

    private fun handleCloseSortMenu() {
        _state.update { currentState ->
            currentState.copy(
                showSortedCollection = currentState.sortQuery != null,

                sortQuery = currentState.sortQuery,
                collection = currentState.collection,
                sortedCollection = currentState.sortedCollection,
            )
        }
    }

    private fun handleCancelSort() {
        _state.update { currentState ->
            currentState.copy(
                sortQuery = null,

                showSortedCollection = currentState.showSortedCollection,
                collection = currentState.collection,
                sortedCollection = currentState.sortedCollection,
            )
        }
    }
}
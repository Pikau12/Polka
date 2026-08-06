package com.polka.android.presentation.coreScreens

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.polka.android.data.model.CollectionItem
import com.polka.android.data.model.SortQuery
import com.polka.android.presentation.common.tiles.ContextMenuAction
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CollectionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val collectionNotSorted: List<CollectionItem> = emptyList(),
    val collectionSorted: List<CollectionItem> = emptyList(),
    val isSortMenuOpen: Boolean = false,
    val sortQuery: SortQuery? = null,
    val searchQuery: String? = null,
    val isDragging: Boolean = false,
    val draggedGameId: Long? = null,
    val dragTargetIndex: Int? = null
)

sealed class CollectionScreenEvent{
    data class OnDoubleClick(val gameId: Long) : CollectionScreenEvent()
    data class OnContextMenuAction(
        val gameId: Long,
        val action: ContextMenuAction
    ) : CollectionScreenEvent()

    data class OnDragStart(val gameId: Long) : CollectionScreenEvent()
    data class OnDragMove(val targetIndex: Int) : CollectionScreenEvent()
    data class OnDragEnd(val gameId: Long) : CollectionScreenEvent()

    data class OnSearchQueryChange(val query: String) : CollectionScreenEvent()
    data class OnSortQueryChange(val query: SortQuery) : CollectionScreenEvent()
}

class CollectionViewModel @Inject constructor(

): ViewModel(){

    private val _state = MutableStateFlow(CollectionState())
    val state: StateFlow<CollectionState> = _state.asStateFlow()

    private fun loadCollection() {} // TODO
    private fun subscribeToUpdates() {} // TODO
    private fun applySortToCollection() {} // TODO

    fun handleEvent(event: CollectionScreenEvent) {
        when (event) {
            is CollectionScreenEvent.OnDoubleClick -> handleDoubleClick(event.gameId)
            is CollectionScreenEvent.OnContextMenuAction -> handleContextMenuAction(
                event.gameId,
                event.action
            )
            is CollectionScreenEvent.OnDragStart -> handleDragStart(event.gameId)
            is CollectionScreenEvent.OnDragMove -> handleDragMove(event.targetIndex)
            is CollectionScreenEvent.OnDragEnd -> handleDragEnd(event.gameId)
            is CollectionScreenEvent.OnSearchQueryChange -> handleSearchQueryChange(event.query)
            is CollectionScreenEvent.OnSortQueryChange -> handleSortQueryChange(event.query)
        }
    }

    private fun handleDoubleClick(gameId: Long){
        // TODO: navigation to GameScreen
    }

    private fun handleContextMenuAction(collectionItemId: Long, action: ContextMenuAction){
        when (action){
            is ContextMenuAction.onStatusClick -> {
                // TODO: open dialog of status changing
            }
            is ContextMenuAction.onRatingClick -> {
                // TODO: open dialog of rating changing / setting
            }
            is ContextMenuAction.onAddSessionClick -> {
                // TODO: navigation to SessionCardScreen
            }
        }
    }

    private fun handleDragStart(gameId: Long){
        val currentState = _state.value

        val originalOrder = currentState.collectionNotSorted.map { collectionItem ->
            collectionItem.displayOrder
        }

        val originalIndex = currentState.collectionNotSorted.indexOfFirst {
            collectionItem ->
            collectionItem.gameId == gameId
        }

        _state.update {
            it.copy(
                isDragging = true,
                draggedGameId = gameId,
                dragTargetIndex = originalIndex
            )
        }
    }

    private fun handleDragMove(targetIndex: Int){

    }

    private fun handleDragEnd(gameId: Long){

    }

    private fun handleSearchQueryChange(query: String){

    }

    private fun handleSortQueryChange(query: SortQuery){

    }
}
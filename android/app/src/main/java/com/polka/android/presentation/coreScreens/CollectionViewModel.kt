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

data class CollectionState(
    val isLoading: Boolean,
    val error: String?,
    val collection: List<CollectionItem>,
    val isSortMenuOpen: Boolean,
    val sortQuery: SortQuery?,
    val searchQuery: String?,
    val isDragging: Boolean = false,
    val draggedCollectionItemId: Int? = null,
    val dragTargetIndex: Int? = null
)

sealed class CollectionScreenEvent{
    data class OnDoubleClick(val gameId: Int) : CollectionScreenEvent()
    data class OnContextMenuAction(
        val collectionItemId: Int,
        val action: ContextMenuAction
    ) : CollectionScreenEvent()

    data class OnDragStart(val collectionItemId: Int) : CollectionScreenEvent()
    data class OnDragMove(val targetIndex: Int) : CollectionScreenEvent()
    data class OnDragEnd(val collectionItemId: Int) : CollectionScreenEvent()

    data class OnSearchQueryChange(val query: String) : CollectionScreenEvent()
    data class OnSortQueryChange(val query: SortQuery) : CollectionScreenEvent()
}

class CollectionViewModel @Inject constructor(

): ViewModel(){

    private val _state = MutableStateFlow(
        CollectionState(
            isLoading = false,
            error = null,
            collection = emptyList(),
            isSortMenuOpen = false,
            sortQuery = null,
            searchQuery = null,
            isDragging = false,
            draggedCollectionItemId = null,
            dragTargetIndex = null
        )
    )
    val state: StateFlow<CollectionState> = _state.asStateFlow()


}
package com.polka.android.presentation.coreScreens

import androidx.lifecycle.ViewModel
import com.polka.android.data.model.CollectionItem
import jakarta.inject.Inject

data class CollectionState(
    val isLoading: Boolean,
    val error: String?,
    val collection: List<CollectionItem>,
    val isSortMenuOpen: Boolean,
    val sortQuery:
)

class CollectionViewModel @Inject constructor(

): ViewModel(){

}
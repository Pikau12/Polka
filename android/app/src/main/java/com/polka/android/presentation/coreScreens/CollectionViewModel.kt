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

//data class CollectionState(
//    
//)

sealed class CollectionScreenEvent{
}

class CollectionViewModel @Inject constructor(

): ViewModel(){

}
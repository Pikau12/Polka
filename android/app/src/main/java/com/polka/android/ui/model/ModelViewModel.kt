package com.polka.android.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.polka.android.data.ModelRepository
import com.polka.android.ui.model.ModelUiState.Error
import com.polka.android.ui.model.ModelUiState.Loading
import com.polka.android.ui.model.ModelUiState.Success
import javax.inject.Inject

@HiltViewModel
class ModelViewModel @Inject constructor(
    private val modelRepository: ModelRepository
) : ViewModel() {

    val uiState: StateFlow<ModelUiState> = modelRepository
        .models.map<List<String>, ModelUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addModel(name: String) {
        viewModelScope.launch {
            modelRepository.add(name)
        }
    }
}

sealed interface ModelUiState {
    object Loading : ModelUiState
    data class Error(val throwable: Throwable) : ModelUiState
    data class Success(val data: List<String>) : ModelUiState
}

package com.polka.android.ui.model


import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.polka.android.data.ModelRepository

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ModelViewModelTest {
    @Test
    fun uiState_initiallyLoading() = runTest {
        val viewModel = ModelViewModel(FakeModelRepository())
        assertEquals(viewModel.uiState.first(), ModelUiState.Loading)
    }

    @Test
    fun uiState_onItemSaved_isDisplayed() = runTest {
        val viewModel = ModelViewModel(FakeModelRepository())
        assertEquals(viewModel.uiState.first(), ModelUiState.Loading)
    }
}

private class FakeModelRepository : ModelRepository {

    private val data = mutableListOf<String>()

    override val models: Flow<List<String>>
        get() = flow { emit(data.toList()) }

    override suspend fun add(name: String) {
        data.add(0, name)
    }
}

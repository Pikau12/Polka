package com.polka.android.ui.model

import com.polka.android.ui.theme.MyApplicationTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey

@Composable
fun ModelScreen(
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ModelViewModel = hiltViewModel(),
) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    if (items is ModelUiState.Success) {
        ModelScreen(
            items = (items as ModelUiState.Success).data,
            onSave = viewModel::addModel,
            modifier = modifier,
        )
    }
}

@Composable
internal fun ModelScreen(
    items: List<String>,
    onSave: (name: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        var nameModel by remember { mutableStateOf("Compose") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                modifier = Modifier.weight(1f),
                value = nameModel,
                onValueChange = { nameModel = it }
            )

            Button(
                modifier = Modifier.width(96.dp),
                onClick = { onSave(nameModel) }
            ) {
                Text("Save")
            }
        }
        items.forEach {
            Text("Saved item: $it")
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        ModelScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
    }
}

@Preview(showBackground = true, widthDp = 340)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        ModelScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
    }
}

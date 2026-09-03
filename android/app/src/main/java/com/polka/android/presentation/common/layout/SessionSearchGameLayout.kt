package com.polka.android.presentation.common.layout

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@Composable
fun SessionSearchGameLayout (
    enabled: Boolean,
    onBackClick: () -> Unit,
    searchString: String,
    onSearchChange: () -> Unit,
    
) {
    BackHandler(enabled) {
        onBackClick()
    }

    if (enabled) {
        Scaffold(

        ) {

        }
    }
}
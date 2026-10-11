package com.polka.android.presentation.coreScreens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun CoreCarousel(
    modifier: Modifier = Modifier,
    navController: NavController,
    initialPage: Int = 1,
) {
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { 3 }
    )

    var isDragging by remember { mutableStateOf(false) }

    HorizontalPager(
        state = pagerState,
        userScrollEnabled = !isDragging,
        modifier = modifier.fillMaxSize()
    ) { page ->
        when (page) {
            0 -> CollectionScreen(
                navController = navController,
                onDragStateChanged = { isDragging = it }
            )
            1 -> SessionsScreen(navController = navController)
            2 -> UserScreen(navController = navController)
        }
    }
}
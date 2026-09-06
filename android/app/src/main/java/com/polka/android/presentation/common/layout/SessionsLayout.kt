package com.polka.android.presentation.common.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.polka.android.R
import com.polka.android.presentation.model.SessionSummary
import com.polka.android.presentation.theme.PolkaSessionCardColors
import com.polka.android.presentation.theme.PolkaStar
import com.polka.android.presentation.theme.PolkaUserRatingStar

@Composable
fun SessionsLayout (
    sessions: List<SessionSummary>,
    onAddSessionClick: () -> Unit,
    onSessionClick: (Long) -> Unit,
    paddingValues: PaddingValues,
) {
    val sessionsState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        state = sessionsState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp) // maybe change value
    ) {
        items(items = sessions, key = { it.sessionId }) { item ->

        }
    }
}
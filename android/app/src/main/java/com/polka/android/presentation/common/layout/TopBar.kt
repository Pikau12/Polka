package com.polka.android.presentation.common.layout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.polka.android.presentation.common.UiConstants.STROKE_SIZE
import com.polka.android.presentation.common.UiConstants.TOPBAR_GAP
import com.polka.android.presentation.common.UiConstants.TOPBAR_HEIGHT
import com.polka.android.presentation.common.buttons.BackButton

/**
 * Consists from left to right: BackButton, [text] on OnSurface background, [rightButton].
 * BackButton use [navController] to popBack to previous screen.
 */
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    text: String,
    navController: NavController,
    rightButton: @Composable (() -> Unit)
) {
    Row(
        modifier = modifier
            .height(TOPBAR_HEIGHT)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(TOPBAR_GAP)
    ) {
        BackButton(navController)

        Card (
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(
                STROKE_SIZE,
                MaterialTheme.colorScheme.surface
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ){
            Box(contentAlignment = Alignment.Center){
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

        rightButton()
    }
}
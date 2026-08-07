package com.polka.android.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AdaptiveTextForGameTile(
    modifier: Modifier = Modifier,
    containerWidthDp: Dp,
    text: String,
    color: androidx.compose.ui.graphics.Color,
    containerPaddingDp: Dp
){
    val measurer = rememberTextMeasurer()

    val density = LocalDensity.current

    val containerPaddingPx = with (density) {containerPaddingDp.value.dp.toPx()}
    val containerWidthPx = with (density) {containerWidthDp.value.dp.toPx()} - 2 * containerPaddingPx

    val largePx = measurer.measure(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        maxLines = 1
    ).size.width.toFloat()

    val mediumPx = measurer.measure(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1
    ).size.width.toFloat()

    val textStyle = if (largePx <= containerWidthPx)
        MaterialTheme.typography.bodyLarge else
            if (mediumPx <= containerWidthPx)
                MaterialTheme.typography.bodyMedium else
                    MaterialTheme.typography.bodySmall

    Text(
        text = text,
        style = textStyle,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        color = color
    )
}
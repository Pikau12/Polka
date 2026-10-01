package com.polka.android.presentation.common.icons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.polka.android.presentation.common.UiConstants.SMALL_ICON_BACKGROUND_CORNER_RADIUS
import com.polka.android.presentation.common.UiConstants.SMALL_ICON_BACKGROUND_SIZE

@Composable
fun SmallIcon(
    @DrawableRes icon: Int,
    iconSize: Dp,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .size(SMALL_ICON_BACKGROUND_SIZE)
            .clip(RoundedCornerShape(SMALL_ICON_BACKGROUND_CORNER_RADIUS))
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = "",
            modifier = Modifier.size(iconSize),
            contentScale = ContentScale.Crop
        )
    }
}
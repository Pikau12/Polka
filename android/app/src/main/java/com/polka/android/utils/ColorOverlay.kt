package com.polka.android.utils

import androidx.compose.ui.graphics.Color

/**
 * Blends elementary float values of colors to create an overlay effect.
 * Take [first] as background and overlay [second] onto it with given [alpha].
 */
fun floatOverlay(
    first: Float,
    second: Float,
    alpha: Float
): Float {
    return first * (1 - alpha) + second * alpha
}

/**
 * Blends colors to create an overlay effect.
 * Take [colorBack] as background and overlay [colorFront] onto it with given [alpha].
 */
fun colorOverlay(
    colorBack: Color,
    colorFront: Color,
    alpha: Float
): Color {
    val alphaNormalize = alpha.coerceIn(0f, 1f)
    return Color(
        red   = floatOverlay(colorBack.red,   colorFront.red,   alphaNormalize),
        green = floatOverlay(colorBack.green, colorFront.green, alphaNormalize),
        blue  = floatOverlay(colorBack.blue,  colorFront.blue,  alphaNormalize),
        alpha = 1f
    )
}

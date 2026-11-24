package com.polka.android.utils

import androidx.compose.ui.graphics.Color

fun getRatingColor(rating: Double) =
    if (rating <= 5) { // red -> orange
        val ratingFloat = rating.toFloat()

        colorOverlay(
            Color(0xFFED1C24),
            Color(0xFFFF7F27),
            (ratingFloat - 1) / 4
        )
    }
    else if (rating <= 6) {
        Color(0xFF3B7CD9)
    }
    else if (rating <= 7) {
        Color(0xFF22B14C)
    }
    else if (rating <= 8) {
        Color(0xFFC4057D)
    }
    else if (rating <= 9) {
        Color(0xFFA349A4)
    }
    else if (rating <= 10) {
        Color(0xFFFFD11A)
    }
    else {
        Color.Transparent
    }
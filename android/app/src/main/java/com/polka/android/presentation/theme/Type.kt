package com.polka.android.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.polka.android.R

val ManropeFontFamily = FontFamily(
    Font(R.font.manrope_regular, FontWeight.Normal),
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_bold, FontWeight.Bold),
    Font(R.font.manrope_semi_bold, FontWeight.SemiBold)
)

val ManropeTypography = Typography(
    // Заголовки
    displayLarge = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp
    ),
    displayMedium = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp
    ),
    displaySmall = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp
    ),

    headlineLarge = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),

    titleLarge = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),

    labelLarge = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    labelMedium = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp
    )
)
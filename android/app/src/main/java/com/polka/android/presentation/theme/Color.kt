package com.polka.android.presentation.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardColors
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.RadioButtonColors
import androidx.compose.ui.graphics.Color
import coil3.compose.AsyncImagePainter

// ===== Main colors =====
val Primary = Color(0xFF9B5860)
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFFEADDFF) // -
val OnPrimaryContainer = Color(0xFF21005D) // -

// ===== Secondary colors =====
val Secondary = Color(0xFF625B71) // -
val OnSecondary = Color(0xFFFFFFFF) // -
val SecondaryContainer = Color(0xFFE8DEF8) // -
val OnSecondaryContainer = Color(0xFF1D192B) // -

// ===== Tertiary colors =====
val Tertiary = Color(0xFF7D5260) // -
val OnTertiary = Color(0xFFFFFFFF) // -
val TertiaryContainer = Color(0xFFFFD8E4) // -
val OnTertiaryContainer = Color(0xFF31111D) // -

// ===== Background =====
val Background = Color(0xFFFFFBFE) // -
val OnBackground = Color(0xFF1C1B1F) // -
val Surface = Color(0xFF484D6D) // TIP: tiles and input container background
val OnSurface = Color(0xFFF5EFF7)  // TIP: text on Surface
val SurfaceVariant = Color(0xFFECE6F0) // TIP: search bar background
val OnSurfaceVariant = Color(0xFF49454F) // TIP: text on SurfaceVariant

// ===== Errors =====
val Error = Color(0xFFBA1A1A) // -
val OnError = Color(0xFFFFFFFF) // -
val ErrorContainer = Color(0xFFFFDAD6) // -
val OnErrorContainer = Color(0xFF410002) // -

// ===== Outline / stroke =====
val Outline = Surface
val OutlineVariant = Color(0xFFCAC4D0) // -

// ===== Custom colors =====
val PolkaOne = Color(0xFF08B2E3)
val PolkaTwo = Color(0xFF30ADAB)
val PolkaThree = Color(0xFF57A773)
val PolkaFour = Color(0xFFEFE9F4)
val PolkaFive = Color(0xFFFFECD8)
val PolkaSix = Color(0xFFEE6352)
val PolkaSeven = Color(0xFFBB8F98)
val PolkaEight = Color(0xFF507A70)
val PolkaNine = Color(0xFF9B5860)
val PolkaTen = Color(0xFF484D6D)
val PolkaEleven = Color(0xFF1CB0C7)
val PolkaTwelve = Color(0xFF44AA8F)

val PolkaFriendsButton = PolkaEleven
val PolkaAccountButton = PolkaTwo
val PolkaRecommendationsButton = PolkaTwelve
val PolkaOverviewButton = PolkaThree
val PolkaSettingsButton = PolkaOne
val PolkaSupportButton = PolkaSeven
val PolkaLogOutButton = PolkaSix
val PolkaTipButton = PolkaEight
val PolkaCancelButton = PolkaSix
val PolkaCurrentItemButton = PolkaThree
val PolkaSupportProjectButton = PolkaSeven
val PolkaBackButton = PolkaSeven
val PolkaAcceptButton = PolkaThree
val PolkaSortButton = PolkaOne
val PolkaOnButton = Color(0xFFF5EFF7)
val PolkaStar = PolkaFive
val PolkaLogInButton = PolkaThree
val PolkaSuccessTextColor = PolkaThree
val PolkaErrorTextColor = PolkaSix

val EmptyColor = Color(0x00FFFFFF)

val PolkaLogInButtonColors = ButtonColors (
    containerColor = PolkaLogInButton,
    contentColor = PolkaOnButton,
    disabledContainerColor = PolkaLogInButton,
    disabledContentColor = PolkaOnButton
)

val PolkaRadioButtonColors = RadioButtonColors(
    selectedColor = Primary,
    unselectedColor = OnSurface,
    disabledSelectedColor = Primary,
    disabledUnselectedColor = OnSurface
)

val PolkaCheckBoxColors = CheckboxColors(
    checkedCheckmarkColor = OnSurface,
    uncheckedCheckmarkColor = EmptyColor,
    checkedBoxColor = Primary,
    uncheckedBoxColor = EmptyColor,
    disabledCheckedBoxColor = Primary,
    disabledUncheckedBoxColor = EmptyColor,
    disabledIndeterminateBoxColor = EmptyColor,
    checkedBorderColor = EmptyColor,
    uncheckedBorderColor = OnSurface,
    disabledBorderColor = EmptyColor,
    disabledUncheckedBorderColor = OnSurface,
    disabledIndeterminateBorderColor = EmptyColor
)

val PolkaTipPopUpColors = CardColors(
    containerColor = Surface,
    contentColor = OnSurface,
    disabledContentColor = Surface,
    disabledContainerColor = OnSurface
)

val PolkaTipButtonColors = ButtonColors(
    containerColor = PolkaTipButton,
    contentColor = PolkaOnButton,
    disabledContainerColor = PolkaTipButton,
    disabledContentColor = PolkaOnButton
)

val PolkaCancelButtonColors = ButtonColors(
    containerColor = PolkaCancelButton,
    contentColor = PolkaOnButton,
    disabledContainerColor = PolkaCancelButton,
    disabledContentColor = PolkaOnButton
)

val PolkaBackButtonColors = ButtonColors(
    containerColor = PolkaBackButton,
    contentColor = PolkaOnButton,
    disabledContainerColor = PolkaBackButton,
    disabledContentColor = PolkaOnButton
)

val PolkaAcceptButtonColors = ButtonColors(
    containerColor = PolkaAcceptButton,
    contentColor = PolkaOnButton,
    disabledContainerColor = PolkaAcceptButton,
    disabledContentColor = PolkaOnButton
)

val PolkaSortButtonColors = ButtonColors(
    containerColor = PolkaSortButton,
    contentColor = PolkaOnButton,
    disabledContainerColor = PolkaSortButton,
    disabledContentColor = PolkaOnButton
)

val PolkaGameTileMenuColors = MenuItemColors(
    textColor = OnSurface,
    leadingIconColor = OnSurface,
    trailingIconColor = OnSurface,
    disabledTextColor = OnSurface,
    disabledLeadingIconColor = OnSurface,
    disabledTrailingIconColor = OnSurface
)

package com.starglen.petcarepro.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PetOrange,
    onPrimary = PetPaper,
    primaryContainer = PetOrangeSoft,
    onPrimaryContainer = PetInk,
    secondary = PetGreen,
    onSecondary = PetPaper,
    secondaryContainer = PetGreenSoft,
    onSecondaryContainer = Color(0xFF153D2C),
    tertiary = PetCoral,
    onTertiary = PetPaper,
    tertiaryContainer = Color(0xFFFFE8E2),
    onTertiaryContainer = Color(0xFF5C2019),
    background = PetCream,
    onBackground = PetInk,
    surface = PetPaper,
    onSurface = PetInk,
    surfaceVariant = PetOrangePale,
    onSurfaceVariant = PetMuted,
    outline = PetBorder,
    error = PetError,
    onError = PetPaper
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB77A),
    onPrimary = Color(0xFF4A2000),
    primaryContainer = Color(0xFF713300),
    onPrimaryContainer = Color(0xFFFFDCC2),
    secondary = Color(0xFFA8D4B7),
    onSecondary = Color(0xFF143D2A),
    secondaryContainer = Color(0xFF2E5740),
    onSecondaryContainer = Color(0xFFC4EBD1),
    tertiary = Color(0xFFFFB4A5),
    onTertiary = Color(0xFF671E13),
    tertiaryContainer = Color(0xFF8A3427),
    onTertiaryContainer = Color(0xFFFFDAD2),
    background = Color(0xFF1E1712),
    onBackground = Color(0xFFF2E5DA),
    surface = Color(0xFF282019),
    onSurface = Color(0xFFF2E5DA),
    surfaceVariant = Color(0xFF3B2E24),
    onSurfaceVariant = Color(0xFFD7C5B7),
    outline = Color(0xFF9C8879),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

private val PetCareShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun PetCareProTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Dynamic color intentionally stays off so PetCare has one coherent identity.
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.background.toArgb()
            window.navigationBarColor = colors.background.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = PetCareShapes,
        content = content
    )
}

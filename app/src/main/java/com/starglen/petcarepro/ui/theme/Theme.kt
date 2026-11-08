package com.starglen.petcarepro.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Forest,
    onPrimary = Paper,
    primaryContainer = Mint,
    onPrimaryContainer = Forest,
    secondary = Terracotta,
    onSecondary = Paper,
    secondaryContainer = Color(0xFFF8E8E1),
    onSecondaryContainer = Color(0xFF542117),
    tertiary = Color(0xFF805600),
    onTertiary = Paper,
    tertiaryContainer = Marigold,
    onTertiaryContainer = Color(0xFF352000),
    background = Canvas,
    onBackground = Ink,
    surface = Paper,
    onSurface = Ink,
    surfaceVariant = Mint,
    onSurfaceVariant = MutedInk,
    outline = Border,
    error = Danger,
    onError = Paper
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9DD4C4),
    onPrimary = Color(0xFF00382F),
    primaryContainer = Color(0xFF145C50),
    onPrimaryContainer = Color(0xFFC0EBDD),
    secondary = Color(0xFFFFB59E),
    onSecondary = Color(0xFF5D190D),
    secondaryContainer = Color(0xFF7B3022),
    onSecondaryContainer = Color(0xFFFFDBD1),
    tertiary = Marigold,
    onTertiary = Color(0xFF422C00),
    background = Color(0xFF111A17),
    onBackground = Color(0xFFE0E9E3),
    surface = Color(0xFF17211D),
    onSurface = Color(0xFFE0E9E3),
    surfaceVariant = Color(0xFF293630),
    onSurfaceVariant = Color(0xFFC0CBC4),
    outline = Color(0xFF89968E),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

private val PetCareShapes = androidx.compose.material3.Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp)
)

@Composable
fun PetCareProTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Dynamic color stays disabled so PetCare has a consistent, recognizable palette.
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = PetCareShapes,
        content = content
    )
}

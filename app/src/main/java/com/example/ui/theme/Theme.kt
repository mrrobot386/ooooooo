package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val StudioDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF00363F),
    onPrimaryContainer = NeonCyan,
    secondary = ElectricViolet,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF3B1360),
    onSecondaryContainer = Color(0xFFE9D5FF),
    tertiary = FilmAmber,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF452600),
    onTertiaryContainer = FilmGold,
    background = StudioBlack,
    onBackground = TextPrimary,
    surface = StudioDark,
    onSurface = TextPrimary,
    surfaceVariant = StudioSurface,
    onSurfaceVariant = TextSecondary,
    outline = StudioBorder,
    error = ActionRed,
    onError = Color.White
)

@Composable
fun VastAiTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = StudioDarkColorScheme,
        typography = Typography,
        content = content
    )
}

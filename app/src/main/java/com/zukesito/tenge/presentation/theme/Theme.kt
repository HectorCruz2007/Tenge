package com.zukesito.tenge.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// tertiary = relleno de barras, surfaceContainerHighest = fondo de barras y badges
private val LightColors = lightColorScheme(
    primary = Graphite,
    onPrimary = Color.White,
    secondaryContainer = Indicator,
    onSecondaryContainer = Ink,
    tertiary = Fill,
    background = Color.White,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    onSurfaceVariant = InkSecondary,
    surfaceVariant = SoftSurface,
    surfaceContainer = NavSurface,
    surfaceContainerHigh = SoftSurface,
    surfaceContainerHighest = Track,
    outline = Outline,
    outlineVariant = Divider
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Ink,
    secondaryContainer = DarkIndicator,
    onSecondaryContainer = DarkInk,
    tertiary = DarkFill,
    background = DarkBackground,
    onBackground = DarkInk,
    surface = DarkBackground,
    onSurface = DarkInk,
    onSurfaceVariant = DarkInkSecondary,
    surfaceVariant = DarkSoftSurface,
    surfaceContainer = DarkNavSurface,
    surfaceContainerHigh = DarkSoftSurface,
    surfaceContainerHighest = DarkTrack,
    outline = DarkOutline,
    outlineVariant = DarkTrack
)

/** Tema Material 3 de la app, compatible con claro y oscuro */
@Composable
fun TengeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = TengeTypography,
        content = content
    )
}

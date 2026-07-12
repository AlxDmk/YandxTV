package com.alxdmk.yandxtv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.alxdmk.yandxtv.domain.model.AppTheme

// TV-optimized color palette — high contrast, visible on large screens
private val TvDarkColorScheme = darkColorScheme(
    primary = Color(0xFF82B1FF),
    onPrimary = Color(0xFF001E6E),
    primaryContainer = Color(0xFF0D369D),
    onPrimaryContainer = Color(0xFFDCE1FF),
    secondary = Color(0xFFBEC6DC),
    onSecondary = Color(0xFF283041),
    secondaryContainer = Color(0xFF3F4759),
    onSecondaryContainer = Color(0xFFDAE2F8),
    background = Color(0xFF1A1B1F),
    onBackground = Color(0xFFE3E2E6),
    surface = Color(0xFF1A1B1F),
    onSurface = Color(0xFFE3E2E6),
    surfaceVariant = Color(0xFF43474E),
    onSurfaceVariant = Color(0xFFC3C7CF),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
)

private val TvLightColorScheme = lightColorScheme(
    primary = Color(0xFF0D5299),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD8E2FF),
    onPrimaryContainer = Color(0xFF001552),
    secondary = Color(0xFF565E72),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDAE2F8),
    onSecondaryContainer = Color(0xFF131C2C),
    background = Color(0xFFFAFAFF),
    onBackground = Color(0xFF1A1B1F),
    surface = Color(0xFFFAFAFF),
    onSurface = Color(0xFF1A1B1F),
    surfaceVariant = Color(0xFFE1E2EC),
    onSurfaceVariant = Color(0xFF44474F),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
)

@Composable
fun YandxTvTheme(
    appTheme: AppTheme = AppTheme.AUTO,
    content: @Composable () -> Unit
) {
    val useDarkTheme = when (appTheme) {
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        AppTheme.AUTO -> isSystemInDarkTheme()
    }

    val colorScheme = if (useDarkTheme) TvDarkColorScheme else TvLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TvTypography,
        content = content
    )
}

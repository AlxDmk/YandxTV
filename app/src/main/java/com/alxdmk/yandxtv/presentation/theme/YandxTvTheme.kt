package com.alxdmk.yandxtv.presentation.theme

import android.app.UiModeManager
import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.alxdmk.yandxtv.domain.model.AppTheme

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF0D1B2A),
    primaryContainer = Color(0xFF1565C0),
    onPrimaryContainer = Color(0xFFE3F2FD),
    secondary = Color(0xFF80CBC4),
    background = Color(0xFF0A0E1A),
    surface = Color(0xFF111827),
    onBackground = Color(0xFFE8EAF6),
    onSurface = Color(0xFFE8EAF6),
    surfaceVariant = Color(0xFF1E293B),
    onSurfaceVariant = Color(0xFFB0BEC5),
    error = Color(0xFFEF9A9A),
    outline = Color(0xFF37474F)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1565C0),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFBBDEFB),
    onPrimaryContainer = Color(0xFF0D47A1),
    secondary = Color(0xFF00695C),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1A1A2E),
    onSurface = Color(0xFF1A1A2E),
    surfaceVariant = Color(0xFFE3F2FD),
    onSurfaceVariant = Color(0xFF546E7A),
    error = Color(0xFFB71C1C),
    outline = Color(0xFFB0BEC5)
)

@Composable
fun YandxTvTheme(
    theme: AppTheme = AppTheme.AUTO,
    content: @Composable () -> Unit
) {
    val darkTheme = when (theme) {
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        AppTheme.AUTO -> isSystemInDarkTheme()
    }
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content
    )
}

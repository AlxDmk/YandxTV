package com.alxdmk.yandxtv.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.alxdmk.yandxtv.domain.model.AppTheme

private val LightColors = lightColorScheme(
    primary = Color(0xFF1565C0),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1E4FF),
    secondary = Color(0xFF535F70),
    background = Color(0xFFF8FDFF),
    surface = Color(0xFFF8FDFF),
    surfaceVariant = Color(0xFFDFE2EB),
    error = Color(0xFFBA1A1A)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9ECAFF),
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF00497D),
    secondary = Color(0xFFBBC7DB),
    background = Color(0xFF1A1C1E),
    surface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFF43474E),
    error = Color(0xFFFFB4AB)
)

@Composable
fun YandxTvTheme(
    appTheme: AppTheme = AppTheme.AUTO,
    content: @Composable () -> Unit
) {
    val darkTheme = when (appTheme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK  -> true
        AppTheme.AUTO  -> isSystemInDarkTheme()
    }
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

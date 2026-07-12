package com.alxdmk.yandxtv.domain.model

enum class AppTheme { LIGHT, DARK, AUTO }

data class UserSettings(
    val theme: AppTheme = AppTheme.AUTO,
    val defaultDesktopUa: Boolean = false,
    val showCredentialWarning: Boolean = true
)

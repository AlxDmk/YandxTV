package com.alxdmk.yandxtv.domain.repository

import com.alxdmk.yandxtv.domain.model.AppTheme
import com.alxdmk.yandxtv.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<UserSettings>
    suspend fun setTheme(theme: AppTheme)
    suspend fun setDefaultDesktopUa(enabled: Boolean)
}

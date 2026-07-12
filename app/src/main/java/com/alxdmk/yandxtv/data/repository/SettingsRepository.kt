package com.alxdmk.yandxtv.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alxdmk.yandxtv.domain.model.AppTheme
import com.alxdmk.yandxtv.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val KEY_THEME = stringPreferencesKey("theme")
        private val KEY_DEFAULT_DESKTOP_UA = booleanPreferencesKey("default_desktop_ua")
        private val KEY_SHOW_CREDENTIAL_WARNING = booleanPreferencesKey("show_credential_warning")
    }

    val settings: Flow<UserSettings> = dataStore.data.map { prefs ->
        UserSettings(
            theme = AppTheme.valueOf(prefs[KEY_THEME] ?: AppTheme.AUTO.name),
            defaultDesktopUa = prefs[KEY_DEFAULT_DESKTOP_UA] ?: false,
            showCredentialWarning = prefs[KEY_SHOW_CREDENTIAL_WARNING] ?: true
        )
    }

    suspend fun setTheme(theme: AppTheme) {
        dataStore.edit { it[KEY_THEME] = theme.name }
    }

    suspend fun setDefaultDesktopUa(value: Boolean) {
        dataStore.edit { it[KEY_DEFAULT_DESKTOP_UA] = value }
    }

    suspend fun setShowCredentialWarning(value: Boolean) {
        dataStore.edit { it[KEY_SHOW_CREDENTIAL_WARNING] = value }
    }
}

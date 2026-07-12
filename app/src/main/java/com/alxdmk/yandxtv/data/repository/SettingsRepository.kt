package com.alxdmk.yandxtv.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.alxdmk.yandxtv.domain.model.AppTheme
import com.alxdmk.yandxtv.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val THEME = stringPreferencesKey("theme")
        val DEFAULT_DESKTOP_UA = booleanPreferencesKey("default_desktop_ua")
        val SHOW_CREDENTIAL_WARNING = booleanPreferencesKey("show_credential_warning")
    }

    val settings: Flow<UserSettings> = dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { prefs ->
            UserSettings(
                theme = AppTheme.valueOf(prefs[Keys.THEME] ?: AppTheme.AUTO.name),
                defaultDesktopUa = prefs[Keys.DEFAULT_DESKTOP_UA] ?: false,
                showCredentialWarning = prefs[Keys.SHOW_CREDENTIAL_WARNING] ?: true
            )
        }

    suspend fun updateTheme(theme: AppTheme) {
        dataStore.edit { it[Keys.THEME] = theme.name }
    }

    suspend fun updateDefaultDesktopUa(enabled: Boolean) {
        dataStore.edit { it[Keys.DEFAULT_DESKTOP_UA] = enabled }
    }

    suspend fun updateShowCredentialWarning(show: Boolean) {
        dataStore.edit { it[Keys.SHOW_CREDENTIAL_WARNING] = show }
    }
}

package com.alxdmk.yandxtv.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alxdmk.yandxtv.domain.model.AppTheme
import com.alxdmk.yandxtv.domain.model.UserSettings
import com.alxdmk.yandxtv.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object Keys {
        val THEME = stringPreferencesKey("theme")
        val DEFAULT_DESKTOP_UA = booleanPreferencesKey("default_desktop_ua")
        val SHOW_CREDENTIAL_WARNING = booleanPreferencesKey("show_credential_warning")
    }

    override fun getSettings(): Flow<UserSettings> = dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { prefs ->
            UserSettings(
                theme = AppTheme.valueOf(prefs[Keys.THEME] ?: AppTheme.AUTO.name),
                defaultDesktopUa = prefs[Keys.DEFAULT_DESKTOP_UA] ?: false,
                showCredentialWarning = prefs[Keys.SHOW_CREDENTIAL_WARNING] ?: true
            )
        }

    override suspend fun setTheme(theme: AppTheme) {
        dataStore.edit { it[Keys.THEME] = theme.name }
    }

    override suspend fun setDefaultDesktopUa(enabled: Boolean) {
        dataStore.edit { it[Keys.DEFAULT_DESKTOP_UA] = enabled }
    }
}

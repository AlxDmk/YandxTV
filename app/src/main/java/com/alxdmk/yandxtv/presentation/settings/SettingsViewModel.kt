package com.alxdmk.yandxtv.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alxdmk.yandxtv.domain.model.AppSettings
import com.alxdmk.yandxtv.domain.model.AppTheme
import com.alxdmk.yandxtv.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<AppSettings> = settingsRepository
        .getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppSettings()
        )

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            settingsRepository.setTheme(theme)
        }
    }

    fun toggleDefaultDesktopUa() {
        viewModelScope.launch {
            settingsRepository.setDefaultDesktopUa(!settings.value.defaultDesktopUa)
        }
    }
}

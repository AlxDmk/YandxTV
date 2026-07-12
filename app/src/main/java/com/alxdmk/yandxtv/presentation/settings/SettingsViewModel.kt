package com.alxdmk.yandxtv.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alxdmk.yandxtv.data.repository.SettingsRepository
import com.alxdmk.yandxtv.domain.model.AppTheme
import com.alxdmk.yandxtv.domain.model.UserSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val settings: StateFlow<UserSettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.Eagerly, UserSettings())

    fun setTheme(theme: AppTheme) = viewModelScope.launch {
        settingsRepository.updateTheme(theme)
    }

    fun toggleDefaultDesktopUa() = viewModelScope.launch {
        settingsRepository.updateDefaultDesktopUa(!settings.value.defaultDesktopUa)
    }
}

package com.alxdmk.yandxtv.presentation.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alxdmk.yandxtv.data.repository.SettingsRepository
import com.alxdmk.yandxtv.data.repository.SiteRepository
import com.alxdmk.yandxtv.domain.model.AppTheme
import com.alxdmk.yandxtv.domain.model.UserSettings
import com.alxdmk.yandxtv.util.CatalogExporter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val siteRepository: SiteRepository
) : ViewModel() {
    val settings: StateFlow<UserSettings> = settingsRepository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserSettings())

    private val _exportResult = MutableStateFlow<String?>(null)
    val exportResult: StateFlow<String?> = _exportResult.asStateFlow()

    private val _importResult = MutableStateFlow<Boolean?>(null)
    val importResult: StateFlow<Boolean?> = _importResult.asStateFlow()

    fun updateTheme(theme: AppTheme) { viewModelScope.launch { settingsRepository.updateTheme(theme) } }

    fun exportCatalog(context: Context) {
        viewModelScope.launch {
            val sites = siteRepository.getAllSites().first()
            val json = CatalogExporter.exportToJson(sites)
            try {
                context.openFileOutput("yandxtv_catalog.json", Context.MODE_PRIVATE).use { it.write(json.toByteArray()) }
                _exportResult.value = context.filesDir.absolutePath + "/yandxtv_catalog.json"
            } catch (e: Exception) { _exportResult.value = null }
        }
    }

    fun importCatalog(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val json = context.contentResolver.openInputStream(uri)?.bufferedReader()?.readText() ?: return@launch
                val sites = CatalogExporter.importFromJson(json)
                if (sites != null) {
                    sites.forEach { siteRepository.saveSite(it) }
                    _importResult.value = true
                } else { _importResult.value = false }
            } catch (e: Exception) { _importResult.value = false }
        }
    }

    fun clearExportResult() { _exportResult.value = null }
    fun clearImportResult() { _importResult.value = null }
}

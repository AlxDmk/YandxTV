package com.alxdmk.yandxtv.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alxdmk.yandxtv.data.repository.SiteRepository
import com.alxdmk.yandxtv.domain.model.Site
import com.alxdmk.yandxtv.util.CatalogExporter
import com.alxdmk.yandxtv.util.DemoData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val sites: List<Site> = emptyList(),
    val isLoading: Boolean = true,
    val exportJson: String? = null,
    val importError: String? = null,
    val importSuccess: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val siteRepository: SiteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadSites()
    }

    private fun loadSites() {
        viewModelScope.launch {
            siteRepository.getAllSites()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .collect { sites ->
                    if (sites.isEmpty() && siteRepository.getSiteCount() == 0) {
                        seedDemoData()
                    } else {
                        _uiState.update { it.copy(sites = sites, isLoading = false) }
                    }
                }
        }
    }

    private suspend fun seedDemoData() {
        DemoData.demoCatalog.forEach { site ->
            siteRepository.saveSite(site)
        }
    }

    fun deleteSite(siteId: Long) {
        viewModelScope.launch {
            siteRepository.deleteSite(siteId)
        }
    }

    fun exportCatalog() {
        viewModelScope.launch {
            val json = CatalogExporter.exportToJson(_uiState.value.sites)
            _uiState.update { it.copy(exportJson = json) }
        }
    }

    fun importCatalog(json: String) {
        viewModelScope.launch {
            val sites = CatalogExporter.importFromJson(json)
            if (sites == null) {
                _uiState.update { it.copy(importError = "Неверный формат файла") }
            } else {
                sites.forEach { siteRepository.saveSite(it) }
                _uiState.update { it.copy(importSuccess = true) }
            }
        }
    }

    fun clearExport() = _uiState.update { it.copy(exportJson = null) }
    fun clearImportResult() = _uiState.update { it.copy(importError = null, importSuccess = false) }
}

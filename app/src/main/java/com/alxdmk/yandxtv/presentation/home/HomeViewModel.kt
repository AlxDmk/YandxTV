package com.alxdmk.yandxtv.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alxdmk.yandxtv.domain.model.Site
import com.alxdmk.yandxtv.domain.repository.CredentialRepository
import com.alxdmk.yandxtv.domain.repository.SiteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

data class HomeUiState(
    val sites: List<Site> = emptyList(),
    val isLoading: Boolean = true,
    val exportJson: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val siteRepository: SiteRepository,
    private val credentialRepository: CredentialRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeSites()
    }

    private fun observeSites() {
        viewModelScope.launch {
            siteRepository.getAllSites().collect { sites ->
                _uiState.update { it.copy(sites = sites, isLoading = false) }
            }
        }
    }

    fun deleteSite(siteId: Long) {
        viewModelScope.launch {
            credentialRepository.deleteCredentials(siteId)
            siteRepository.deleteSite(siteId)
        }
    }

    fun exportCatalog() {
        viewModelScope.launch {
            val sites = siteRepository.getAllSitesOnce()
            val exportData = sites.map { site ->
                mapOf(
                    "title" to site.title,
                    "url" to site.url,
                    "description" to site.description,
                    "iconLabel" to site.iconLabel,
                    "colorHex" to site.colorHex,
                    "useDesktopUserAgent" to site.useDesktopUserAgent.toString(),
                    "allowAutofill" to site.allowAutofill.toString()
                )
            }
            val json = Json { prettyPrint = true }.encodeToString(exportData)
            _uiState.update { it.copy(exportJson = json) }
        }
    }

    fun clearExport() {
        _uiState.update { it.copy(exportJson = null) }
    }
}

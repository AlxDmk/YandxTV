package com.alxdmk.yandxtv.presentation.viewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alxdmk.yandxtv.domain.model.Credential
import com.alxdmk.yandxtv.domain.model.Site
import com.alxdmk.yandxtv.domain.repository.CredentialRepository
import com.alxdmk.yandxtv.domain.repository.SiteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SiteViewerUiState(
    val site: Site? = null,
    val currentUrl: String = "",
    val pageTitle: String = "",
    val isLoading: Boolean = true,
    val useDesktopUa: Boolean = false,
    val hasCredentials: Boolean = false,
    val credential: Credential? = null,
    val showCredentialMenu: Boolean = false
)

@HiltViewModel
class SiteViewerViewModel @Inject constructor(
    private val siteRepository: SiteRepository,
    private val credentialRepository: CredentialRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SiteViewerUiState())
    val uiState: StateFlow<SiteViewerUiState> = _uiState.asStateFlow()

    fun loadSite(siteId: Long) {
        viewModelScope.launch {
            val site = siteRepository.getSiteById(siteId) ?: return@launch
            val credential = credentialRepository.getCredentials(siteId)
            _uiState.update {
                it.copy(
                    site = site,
                    currentUrl = site.url,
                    useDesktopUa = site.useDesktopUserAgent,
                    hasCredentials = site.hasCredentials,
                    credential = credential
                )
            }
        }
    }

    fun onPageStarted(url: String) {
        _uiState.update { it.copy(currentUrl = url, isLoading = true) }
    }

    fun onPageFinished(url: String, title: String) {
        _uiState.update { it.copy(currentUrl = url, pageTitle = title, isLoading = false) }
    }

    fun toggleDesktopUa() {
        _uiState.update { it.copy(useDesktopUa = !it.useDesktopUa) }
    }

    fun toggleCredentialMenu() {
        _uiState.update { it.copy(showCredentialMenu = !it.showCredentialMenu) }
    }

    fun dismissCredentialMenu() {
        _uiState.update { it.copy(showCredentialMenu = false) }
    }
}

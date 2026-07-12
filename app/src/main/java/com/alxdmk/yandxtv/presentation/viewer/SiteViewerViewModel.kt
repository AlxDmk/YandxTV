package com.alxdmk.yandxtv.presentation.viewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alxdmk.yandxtv.data.repository.CredentialRepository
import com.alxdmk.yandxtv.data.repository.SiteRepository
import com.alxdmk.yandxtv.domain.model.Credential
import com.alxdmk.yandxtv.domain.model.Site
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ViewerUiState(
    val site: Site? = null,
    val isLoading: Boolean = true,
    val currentUrl: String = "",
    val pageTitle: String = "",
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

    private val _uiState = MutableStateFlow(ViewerUiState())
    val uiState: StateFlow<ViewerUiState> = _uiState.asStateFlow()

    fun loadSite(siteId: Long) {
        viewModelScope.launch {
            val site = siteRepository.getSiteById(siteId) ?: return@launch
            val credential = credentialRepository.getCredential(siteId)
            _uiState.update {
                it.copy(
                    site = site,
                    currentUrl = site.url,
                    useDesktopUa = site.useDesktopUserAgent,
                    hasCredentials = credential != null,
                    credential = credential
                )
            }
        }
    }

    fun onPageStarted(url: String) = _uiState.update { it.copy(isLoading = true, currentUrl = url) }
    fun onPageFinished(url: String, title: String) = _uiState.update {
        it.copy(isLoading = false, currentUrl = url, pageTitle = title)
    }

    fun toggleDesktopUa() = _uiState.update { it.copy(useDesktopUa = !it.useDesktopUa) }
    fun toggleCredentialMenu() = _uiState.update { it.copy(showCredentialMenu = !it.showCredentialMenu) }
    fun dismissCredentialMenu() = _uiState.update { it.copy(showCredentialMenu = false) }
}

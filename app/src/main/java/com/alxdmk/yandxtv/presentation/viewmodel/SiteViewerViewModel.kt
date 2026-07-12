package com.alxdmk.yandxtv.presentation.viewmodel

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
    val credential: Credential? = null,
    val isDesktopUa: Boolean = false,
    val isLoading: Boolean = true,
    val showFillDialog: Boolean = false
)

@HiltViewModel
class SiteViewerViewModel @Inject constructor(
    private val siteRepository: SiteRepository,
    private val credentialRepository: CredentialRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ViewerUiState())
    val state: StateFlow<ViewerUiState> = _state.asStateFlow()

    fun loadSite(siteId: Long) {
        viewModelScope.launch {
            val site = siteRepository.getSiteById(siteId)
            val cred = credentialRepository.getCredential(siteId)
            _state.update { it.copy(site = site, credential = cred, isDesktopUa = site?.useDesktopUserAgent ?: false, isLoading = false) }
        }
    }

    fun toggleDesktopUa() = _state.update { it.copy(isDesktopUa = !it.isDesktopUa) }
    fun setLoading(loading: Boolean) = _state.update { it.copy(isLoading = loading) }
    fun showFillDialog() = _state.update { it.copy(showFillDialog = true) }
    fun hideFillDialog() = _state.update { it.copy(showFillDialog = false) }
}

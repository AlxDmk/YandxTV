package com.alxdmk.yandxtv.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alxdmk.yandxtv.data.repository.SiteRepository
import com.alxdmk.yandxtv.domain.model.Site
import com.alxdmk.yandxtv.util.DemoData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val sites: List<Site> = emptyList(),
    val isLoading: Boolean = true,
    val selectedSiteId: Long? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val siteRepository: SiteRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeSites()
        seedDemoDataIfEmpty()
    }

    private fun observeSites() {
        viewModelScope.launch {
            siteRepository.getAllSites().collect { sites ->
                _uiState.update { it.copy(sites = sites, isLoading = false) }
            }
        }
    }

    private fun seedDemoDataIfEmpty() {
        viewModelScope.launch {
            if (siteRepository.getSiteCount() == 0) {
                DemoData.demoCatalog.forEach { siteRepository.saveSite(it) }
            }
        }
    }

    fun deleteSite(siteId: Long) {
        viewModelScope.launch { siteRepository.deleteSite(siteId) }
    }

    fun selectSite(siteId: Long?) {
        _uiState.update { it.copy(selectedSiteId = siteId) }
    }
}

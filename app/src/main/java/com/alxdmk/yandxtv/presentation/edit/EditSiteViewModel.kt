package com.alxdmk.yandxtv.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alxdmk.yandxtv.domain.model.Site
import com.alxdmk.yandxtv.domain.repository.SiteRepository
import com.alxdmk.yandxtv.util.UrlUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditSiteUiState(
    val isEditMode: Boolean = false,
    val title: String = "",
    val titleError: String? = null,
    val url: String = "",
    val urlError: String? = null,
    val description: String = "",
    val iconLabel: String = "",
    val colorHex: String = "#1565C0",
    val useDesktopUserAgent: Boolean = false,
    val allowAutofill: Boolean = true,
    val isSaving: Boolean = false,
    val savedId: Long? = null
)

@HiltViewModel
class EditSiteViewModel @Inject constructor(
    private val siteRepository: SiteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditSiteUiState())
    val uiState: StateFlow<EditSiteUiState> = _uiState.asStateFlow()

    fun loadSite(siteId: Long) {
        viewModelScope.launch {
            val site = siteRepository.getSiteById(siteId) ?: return@launch
            _uiState.update {
                it.copy(
                    isEditMode = true,
                    title = site.title,
                    url = site.url,
                    description = site.description,
                    iconLabel = site.iconLabel,
                    colorHex = site.colorHex,
                    useDesktopUserAgent = site.useDesktopUserAgent,
                    allowAutofill = site.allowAutofill
                )
            }
        }
    }

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value, titleError = null) }
    }

    fun onUrlChange(value: String) {
        _uiState.update { it.copy(url = value, urlError = null) }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value) }
    }

    fun onIconLabelChange(value: String) {
        _uiState.update { it.copy(iconLabel = value.take(2)) }
    }

    fun onDesktopUaToggle() {
        _uiState.update { it.copy(useDesktopUserAgent = !it.useDesktopUserAgent) }
    }

    fun onAutofillToggle() {
        _uiState.update { it.copy(allowAutofill = !it.allowAutofill) }
    }

    fun saveSite(existingSiteId: Long?) {
        val state = _uiState.value
        var hasError = false

        if (state.title.isBlank()) {
            _uiState.update { it.copy(titleError = "Введите название") }
            hasError = true
        }

        val normalizedUrl = UrlUtils.normalizeUrl(state.url)
        if (normalizedUrl == null) {
            _uiState.update { it.copy(urlError = "Введите корректный URL (например https://example.com)") }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val site = Site(
                id = existingSiteId ?: 0L,
                title = state.title.trim(),
                url = normalizedUrl!!,
                description = state.description.trim(),
                iconLabel = state.iconLabel.trim(),
                colorHex = state.colorHex,
                useDesktopUserAgent = state.useDesktopUserAgent,
                allowAutofill = state.allowAutofill,
                hasCredentials = false
            )
            val savedId = if (existingSiteId != null && existingSiteId != 0L) {
                siteRepository.updateSite(site)
                existingSiteId
            } else {
                siteRepository.saveSite(site)
            }
            _uiState.update { it.copy(isSaving = false, savedId = savedId) }
        }
    }
}

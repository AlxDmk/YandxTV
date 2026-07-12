package com.alxdmk.yandxtv.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alxdmk.yandxtv.data.repository.SiteRepository
import com.alxdmk.yandxtv.domain.model.Site
import com.alxdmk.yandxtv.util.UrlUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditSiteUiState(
    val title: String = "",
    val url: String = "",
    val description: String = "",
    val iconLabel: String = "",
    val colorHex: String = "#1565C0",
    val useDesktopUserAgent: Boolean = false,
    val allowAutofill: Boolean = true,
    val titleError: String? = null,
    val urlError: String? = null,
    val isSaving: Boolean = false,
    val savedId: Long? = null,
    val isEditMode: Boolean = false
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
                    title = site.title,
                    url = site.url,
                    description = site.description,
                    iconLabel = site.iconLabel,
                    colorHex = site.colorHex,
                    useDesktopUserAgent = site.useDesktopUserAgent,
                    allowAutofill = site.allowAutofill,
                    isEditMode = true
                )
            }
        }
    }

    fun onTitleChange(v: String) = _uiState.update { it.copy(title = v, titleError = null) }
    fun onUrlChange(v: String) = _uiState.update { it.copy(url = v, urlError = null) }
    fun onDescriptionChange(v: String) = _uiState.update { it.copy(description = v) }
    fun onIconLabelChange(v: String) = _uiState.update { it.copy(iconLabel = v.take(2)) }
    fun onColorHexChange(v: String) = _uiState.update { it.copy(colorHex = v) }
    fun onDesktopUaToggle() = _uiState.update { it.copy(useDesktopUserAgent = !it.useDesktopUserAgent) }
    fun onAutofillToggle() = _uiState.update { it.copy(allowAutofill = !it.allowAutofill) }

    fun saveSite(existingSiteId: Long?) {
        val state = _uiState.value
        var hasError = false

        if (state.title.isBlank()) {
            _uiState.update { it.copy(titleError = "Название не может быть пустым") }
            hasError = true
        }

        val normalizedUrl = UrlUtils.normalizeUrl(state.url)
        if (normalizedUrl == null) {
            _uiState.update { it.copy(urlError = "Введите корректный URL (например: example.com)") }
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
                allowAutofill = state.allowAutofill
            )
            val id = siteRepository.saveSite(site)
            _uiState.update { it.copy(isSaving = false, savedId = id) }
        }
    }
}

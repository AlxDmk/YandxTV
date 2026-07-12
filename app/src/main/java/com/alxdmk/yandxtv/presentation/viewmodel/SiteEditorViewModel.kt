package com.alxdmk.yandxtv.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alxdmk.yandxtv.data.repository.SiteRepository
import com.alxdmk.yandxtv.domain.model.Site
import com.alxdmk.yandxtv.util.UrlUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SiteEditorUiState(
    val isEditMode: Boolean = false,
    val title: String = "",
    val url: String = "",
    val description: String = "",
    val iconLabel: String = "",
    val colorHex: String = "#1565C0",
    val useDesktopUserAgent: Boolean = false,
    val allowAutofill: Boolean = true,
    val urlError: String? = null,
    val titleError: String? = null,
    val isSaving: Boolean = false,
    val saved: Boolean = false
)

@HiltViewModel
class SiteEditorViewModel @Inject constructor(
    private val siteRepository: SiteRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SiteEditorUiState())
    val state: StateFlow<SiteEditorUiState> = _state.asStateFlow()

    fun loadSite(siteId: Long) {
        viewModelScope.launch {
            siteRepository.getSiteById(siteId)?.let { site ->
                _state.update {
                    it.copy(
                        isEditMode = true, title = site.title, url = site.url,
                        description = site.description, iconLabel = site.iconLabel,
                        colorHex = site.colorHex, useDesktopUserAgent = site.useDesktopUserAgent,
                        allowAutofill = site.allowAutofill
                    )
                }
            }
        }
    }

    fun onTitleChange(v: String) = _state.update { it.copy(title = v, titleError = null) }
    fun onUrlChange(v: String) = _state.update { it.copy(url = v, urlError = null) }
    fun onDescriptionChange(v: String) = _state.update { it.copy(description = v) }
    fun onIconLabelChange(v: String) = _state.update { it.copy(iconLabel = v.take(2)) }
    fun onColorChange(v: String) = _state.update { it.copy(colorHex = v) }
    fun onDesktopUaChange(v: Boolean) = _state.update { it.copy(useDesktopUserAgent = v) }
    fun onAutofillChange(v: Boolean) = _state.update { it.copy(allowAutofill = v) }

    fun save(existingSiteId: Long?) {
        val s = _state.value
        var hasError = false
        if (s.title.isBlank()) { _state.update { it.copy(titleError = "Название обязательно") }; hasError = true }
        val normalizedUrl = UrlUtils.normalizeUrl(s.url)
        if (normalizedUrl == null) { _state.update { it.copy(urlError = "Некорректный URL") }; hasError = true }
        if (hasError) return
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            siteRepository.saveSite(
                Site(
                    id = existingSiteId ?: 0L,
                    title = s.title.trim(),
                    url = normalizedUrl!!,
                    description = s.description.trim(),
                    iconLabel = s.iconLabel.trim().ifBlank { s.title.take(1).uppercase() },
                    colorHex = s.colorHex,
                    useDesktopUserAgent = s.useDesktopUserAgent,
                    allowAutofill = s.allowAutofill
                )
            )
            _state.update { it.copy(isSaving = false, saved = true) }
        }
    }
}

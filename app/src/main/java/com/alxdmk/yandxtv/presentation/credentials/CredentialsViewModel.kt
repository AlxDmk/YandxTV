package com.alxdmk.yandxtv.presentation.credentials

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

data class CredentialsUiState(
    val site: Site? = null,
    val username: String = "",
    val password: String = "",
    val note: String = "",
    val passwordVisible: Boolean = false,
    val hasExistingCredentials: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)

@HiltViewModel
class CredentialsViewModel @Inject constructor(
    private val credentialRepository: CredentialRepository,
    private val siteRepository: SiteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CredentialsUiState())
    val uiState: StateFlow<CredentialsUiState> = _uiState.asStateFlow()

    fun loadForSite(siteId: Long) {
        viewModelScope.launch {
            val site = siteRepository.getSiteById(siteId)
            val existing = credentialRepository.getCredentials(siteId)
            _uiState.update {
                it.copy(
                    site = site,
                    username = existing?.username ?: "",
                    password = existing?.password ?: "",
                    note = existing?.note ?: "",
                    hasExistingCredentials = existing != null
                )
            }
        }
    }

    fun onUsernameChange(value: String) = _uiState.update { it.copy(username = value) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value) }
    fun onNoteChange(value: String) = _uiState.update { it.copy(note = value) }
    fun togglePasswordVisible() = _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }

    fun saveCredentials(siteId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val credential = Credential(
                siteId = siteId,
                username = _uiState.value.username,
                password = _uiState.value.password,
                note = _uiState.value.note
            )
            credentialRepository.saveCredentials(credential)
            // Update hasCredentials flag on site
            siteRepository.getSiteById(siteId)?.let { site ->
                siteRepository.updateSite(site.copy(hasCredentials = true))
            }
            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }

    fun deleteCredentials(siteId: Long) {
        viewModelScope.launch {
            credentialRepository.deleteCredentials(siteId)
            siteRepository.getSiteById(siteId)?.let { site ->
                siteRepository.updateSite(site.copy(hasCredentials = false))
            }
            _uiState.update {
                it.copy(
                    username = "", password = "", note = "",
                    hasExistingCredentials = false, isSaved = true
                )
            }
        }
    }
}

package com.alxdmk.yandxtv.presentation.credentials

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

data class CredentialsUiState(
    val site: Site? = null,
    val username: String = "",
    val password: String = "",
    val note: String = "",
    val passwordVisible: Boolean = false,
    val isSaved: Boolean = false,
    val hasExistingCredentials: Boolean = false
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
            val existing = credentialRepository.getCredential(siteId)
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

    fun onUsernameChange(v: String) = _uiState.update { it.copy(username = v) }
    fun onPasswordChange(v: String) = _uiState.update { it.copy(password = v) }
    fun onNoteChange(v: String) = _uiState.update { it.copy(note = v) }
    fun togglePasswordVisible() = _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }

    fun saveCredentials(siteId: Long) {
        val state = _uiState.value
        credentialRepository.saveCredential(
            Credential(
                siteId = siteId,
                username = state.username,
                password = state.password,
                note = state.note
            )
        )
        _uiState.update { it.copy(isSaved = true, hasExistingCredentials = true) }
    }

    fun deleteCredentials(siteId: Long) {
        credentialRepository.deleteCredential(siteId)
        _uiState.update { it.copy(username = "", password = "", note = "", hasExistingCredentials = false) }
    }
}

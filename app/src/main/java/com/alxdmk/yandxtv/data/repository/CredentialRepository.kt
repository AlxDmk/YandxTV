package com.alxdmk.yandxtv.data.repository

import com.alxdmk.yandxtv.data.security.EncryptedCredentialStorage
import com.alxdmk.yandxtv.domain.model.Credential
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialRepository @Inject constructor(
    private val storage: EncryptedCredentialStorage
) {
    fun getCredential(siteId: Long): Credential? = storage.getCredential(siteId)
    fun saveCredential(credential: Credential) = storage.saveCredential(credential)
    fun deleteCredential(siteId: Long) = storage.deleteCredential(siteId)
    fun hasCredential(siteId: Long): Boolean = storage.hasCredential(siteId)
}

package com.alxdmk.yandxtv.data.repository

import com.alxdmk.yandxtv.data.security.EncryptedCredentialStorage
import com.alxdmk.yandxtv.domain.model.Credential
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialRepository @Inject constructor(
    private val storage: EncryptedCredentialStorage
) {
    fun load(siteId: Long): Credential? = storage.loadCredential(siteId)
    fun save(credential: Credential) = storage.saveCredential(credential)
    fun delete(siteId: Long) = storage.deleteCredential(siteId)
    fun has(siteId: Long): Boolean = storage.hasCredential(siteId)
}

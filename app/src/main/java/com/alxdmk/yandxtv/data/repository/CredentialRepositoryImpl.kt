package com.alxdmk.yandxtv.data.repository

import com.alxdmk.yandxtv.data.security.EncryptedCredentialStorage
import com.alxdmk.yandxtv.domain.model.Credential
import com.alxdmk.yandxtv.domain.repository.CredentialRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialRepositoryImpl @Inject constructor(
    private val storage: EncryptedCredentialStorage
) : CredentialRepository {

    override fun getCredentials(siteId: Long): Credential? =
        storage.getCredential(siteId)

    override fun saveCredentials(credential: Credential) =
        storage.saveCredential(credential)

    override fun deleteCredentials(siteId: Long) =
        storage.deleteCredential(siteId)

    override fun hasCredential(siteId: Long): Boolean =
        storage.hasCredential(siteId)
}

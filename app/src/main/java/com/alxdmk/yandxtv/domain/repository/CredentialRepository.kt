package com.alxdmk.yandxtv.domain.repository

import com.alxdmk.yandxtv.domain.model.Credential

interface CredentialRepository {
    fun getCredentials(siteId: Long): Credential?
    fun saveCredentials(credential: Credential)
    fun deleteCredentials(siteId: Long)
    fun hasCredential(siteId: Long): Boolean
}

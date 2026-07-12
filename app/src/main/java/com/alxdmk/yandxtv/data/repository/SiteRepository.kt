package com.alxdmk.yandxtv.data.repository

import com.alxdmk.yandxtv.data.db.SiteDao
import com.alxdmk.yandxtv.data.db.SiteEntity
import com.alxdmk.yandxtv.data.security.EncryptedCredentialStorage
import com.alxdmk.yandxtv.domain.model.Site
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SiteRepository @Inject constructor(
    private val siteDao: SiteDao,
    private val credentialStorage: EncryptedCredentialStorage
) {
    fun getAllSites(): Flow<List<Site>> = siteDao.getAllSites().map { entities ->
        entities.map { it.toDomain(credentialStorage.hasCredential(it.id)) }
    }

    suspend fun getSiteById(id: Long): Site? =
        siteDao.getSiteById(id)?.toDomain(credentialStorage.hasCredential(id))

    suspend fun saveSite(site: Site): Long {
        return if (site.id == 0L) {
            siteDao.insertSite(site.toEntity())
        } else {
            siteDao.updateSite(site.toEntity())
            site.id
        }
    }

    suspend fun deleteSite(siteId: Long) {
        siteDao.deleteSiteById(siteId)
        credentialStorage.deleteCredential(siteId)
    }

    suspend fun getSiteCount(): Int = siteDao.getSiteCount()

    private fun SiteEntity.toDomain(hasCredentials: Boolean) = Site(
        id = id,
        title = title,
        url = url,
        description = description,
        iconLabel = iconLabel,
        colorHex = colorHex,
        useDesktopUserAgent = useDesktopUserAgent,
        allowAutofill = allowAutofill,
        hasCredentials = hasCredentials,
        sortOrder = sortOrder,
        createdAt = createdAt
    )

    private fun Site.toEntity() = SiteEntity(
        id = id,
        title = title,
        url = url,
        description = description,
        iconLabel = iconLabel,
        colorHex = colorHex,
        useDesktopUserAgent = useDesktopUserAgent,
        allowAutofill = allowAutofill,
        sortOrder = sortOrder,
        createdAt = createdAt
    )
}

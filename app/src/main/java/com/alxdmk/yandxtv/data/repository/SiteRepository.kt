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
    val sites: Flow<List<Site>> = siteDao.getAllSites().map { entities ->
        entities.map { entity ->
            entity.toDomain(hasCredentials = credentialStorage.hasCredential(entity.id))
        }
    }

    suspend fun getSiteById(id: Long): Site? {
        return siteDao.getSiteById(id)?.toDomain(
            hasCredentials = credentialStorage.hasCredential(id)
        )
    }

    suspend fun insertSite(site: Site): Long {
        return siteDao.insertSite(site.toEntity())
    }

    suspend fun updateSite(site: Site) {
        siteDao.updateSite(site.toEntity())
    }

    suspend fun deleteSite(site: Site) {
        siteDao.deleteSiteById(site.id)
        credentialStorage.deleteCredential(site.id)
    }

    suspend fun getSiteCount(): Int = siteDao.getSiteCount()

    private fun SiteEntity.toDomain(hasCredentials: Boolean) = Site(
        id = id, title = title, url = url, description = description,
        iconLabel = iconLabel, colorHex = colorHex,
        useDesktopUserAgent = useDesktopUserAgent, allowAutofill = allowAutofill,
        hasCredentials = hasCredentials, sortOrder = sortOrder, createdAt = createdAt
    )

    private fun Site.toEntity() = SiteEntity(
        id = id, title = title, url = url, description = description,
        iconLabel = iconLabel, colorHex = colorHex,
        useDesktopUserAgent = useDesktopUserAgent, allowAutofill = allowAutofill,
        sortOrder = sortOrder, createdAt = createdAt
    )
}

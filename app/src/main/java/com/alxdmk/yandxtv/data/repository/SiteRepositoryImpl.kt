package com.alxdmk.yandxtv.data.repository

import com.alxdmk.yandxtv.data.db.SiteDao
import com.alxdmk.yandxtv.data.db.SiteEntity
import com.alxdmk.yandxtv.data.security.EncryptedCredentialStorage
import com.alxdmk.yandxtv.domain.model.Site
import com.alxdmk.yandxtv.domain.repository.SiteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SiteRepositoryImpl @Inject constructor(
    private val siteDao: SiteDao,
    private val credentialStorage: EncryptedCredentialStorage
) : SiteRepository {

    override fun getAllSites(): Flow<List<Site>> =
        siteDao.getAllSites().map { entities ->
            entities.map { it.toDomain(credentialStorage.hasCredential(it.id)) }
        }

    override suspend fun getAllSitesOnce(): List<Site> =
        siteDao.getAllSites().first().map {
            it.toDomain(credentialStorage.hasCredential(it.id))
        }

    override suspend fun getSiteById(id: Long): Site? =
        siteDao.getSiteById(id)?.toDomain(credentialStorage.hasCredential(id))

    override suspend fun saveSite(site: Site): Long {
        return if (site.id == 0L) {
            siteDao.insertSite(site.toEntity())
        } else {
            siteDao.updateSite(site.toEntity())
            site.id
        }
    }

    override suspend fun updateSite(site: Site) {
        siteDao.updateSite(site.toEntity())
    }

    override suspend fun deleteSite(siteId: Long) {
        siteDao.deleteSiteById(siteId)
        credentialStorage.deleteCredential(siteId)
    }

    override suspend fun getSiteCount(): Int = siteDao.getSiteCount()

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

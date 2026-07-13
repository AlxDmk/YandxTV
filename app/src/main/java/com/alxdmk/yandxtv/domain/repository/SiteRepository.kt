package com.alxdmk.yandxtv.domain.repository

import com.alxdmk.yandxtv.domain.model.Site
import kotlinx.coroutines.flow.Flow

interface SiteRepository {
    fun getAllSites(): Flow<List<Site>>
    suspend fun getAllSitesOnce(): List<Site>
    suspend fun getSiteById(id: Long): Site?
    suspend fun saveSite(site: Site): Long
    suspend fun updateSite(site: Site)
    suspend fun deleteSite(siteId: Long)
    suspend fun getSiteCount(): Int
}

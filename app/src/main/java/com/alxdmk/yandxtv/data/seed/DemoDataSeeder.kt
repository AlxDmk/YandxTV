package com.alxdmk.yandxtv.data.seed

import com.alxdmk.yandxtv.data.db.SiteDao
import com.alxdmk.yandxtv.data.db.SiteEntity
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoDataSeeder @Inject constructor(
    private val siteDao: SiteDao
) {
    suspend fun seedIfEmpty() {
        val existing = siteDao.getAllSites().first()
        if (existing.isNotEmpty()) return

        val demoSites = listOf(
            SiteEntity(
                title = "Example Domain",
                url = "https://example.org",
                description = "Официальный демонстрационный домен IANA — пример нейтрального сайта",
                iconLabel = "EX",
                colorHex = "#1565C0",
                useDesktopUserAgent = false,
                allowAutofill = false
            ),
            SiteEntity(
                title = "Wikipedia",
                url = "https://ru.wikipedia.org",
                description = "Свободная энциклопедия на русском языке",
                iconLabel = "W",
                colorHex = "#37474F",
                useDesktopUserAgent = true,
                allowAutofill = false
            ),
            SiteEntity(
                title = "OpenStreetMap",
                url = "https://www.openstreetmap.org",
                description = "Открытая карта мира — редактируемая всеми",
                iconLabel = "OS",
                colorHex = "#2E7D32",
                useDesktopUserAgent = true,
                allowAutofill = false
            )
        )

        demoSites.forEach { siteDao.insertSite(it) }
    }
}

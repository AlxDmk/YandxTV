package com.alxdmk.yandxtv.util

import com.alxdmk.yandxtv.domain.model.Site
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Export/import site catalog as JSON.
 * Credentials are intentionally excluded from export for security.
 */
object CatalogExporter {

    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    data class ExportDto(
        val version: Int = 1,
        val exportedAt: Long = System.currentTimeMillis(),
        val sites: List<SiteDto>
    )

    data class SiteDto(
        val title: String,
        val url: String,
        val description: String,
        val iconLabel: String,
        val colorHex: String,
        val useDesktopUserAgent: Boolean,
        val allowAutofill: Boolean,
        val sortOrder: Int
    )

    fun exportToJson(sites: List<Site>): String {
        val dto = ExportDto(
            sites = sites.map { it.toDto() }
        )
        return gson.toJson(dto)
    }

    fun importFromJson(json: String): List<Site>? {
        return try {
            val dto = gson.fromJson(json, ExportDto::class.java)
            dto.sites.map { it.toSite() }
        } catch (e: Exception) {
            null
        }
    }

    private fun Site.toDto() = SiteDto(
        title = title,
        url = url,
        description = description,
        iconLabel = iconLabel,
        colorHex = colorHex,
        useDesktopUserAgent = useDesktopUserAgent,
        allowAutofill = allowAutofill,
        sortOrder = sortOrder
    )

    private fun SiteDto.toSite() = Site(
        title = title,
        url = url,
        description = description,
        iconLabel = iconLabel,
        colorHex = colorHex,
        useDesktopUserAgent = useDesktopUserAgent,
        allowAutofill = allowAutofill,
        sortOrder = sortOrder
    )
}

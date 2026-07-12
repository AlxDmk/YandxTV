package com.alxdmk.yandxtv.util

import com.alxdmk.yandxtv.domain.model.Site
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

/**
 * Export/import site catalogue as JSON.
 * Credentials are intentionally excluded from export.
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

    fun toJson(sites: List<Site>): String {
        val dto = ExportDto(
            sites = sites.map { site ->
                SiteDto(
                    title = site.title,
                    url = site.url,
                    description = site.description,
                    iconLabel = site.iconLabel,
                    colorHex = site.colorHex,
                    useDesktopUserAgent = site.useDesktopUserAgent,
                    allowAutofill = site.allowAutofill,
                    sortOrder = site.sortOrder
                )
            }
        )
        return gson.toJson(dto)
    }

    fun fromJson(json: String): List<Site> {
        val type = object : TypeToken<ExportDto>() {}.type
        val dto: ExportDto = gson.fromJson(json, type)
        return dto.sites.map { d ->
            Site(
                title = d.title,
                url = d.url,
                description = d.description,
                iconLabel = d.iconLabel,
                colorHex = d.colorHex,
                useDesktopUserAgent = d.useDesktopUserAgent,
                allowAutofill = d.allowAutofill,
                sortOrder = d.sortOrder
            )
        }
    }
}

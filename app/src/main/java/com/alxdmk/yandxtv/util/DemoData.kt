package com.alxdmk.yandxtv.util

import com.alxdmk.yandxtv.domain.model.Site

/**
 * Demo catalog seeded on first launch.
 * Contains only neutral, publicly available sites.
 * User can delete or modify these at any time.
 */
object DemoData {
    val demoCatalog = listOf(
        Site(
            title = "Example Domain",
            url = "https://example.org",
            description = "Официальный пример домена IANA",
            iconLabel = "E",
            colorHex = "#1565C0",
            sortOrder = 0
        ),
        Site(
            title = "Wikipedia",
            url = "https://ru.wikipedia.org",
            description = "Свободная энциклопедия",
            iconLabel = "W",
            colorHex = "#424242",
            useDesktopUserAgent = true,
            sortOrder = 1
        ),
        Site(
            title = "Archive.org",
            url = "https://archive.org",
            description = "Интернет-архив — бесплатные книги, фильмы, музыка",
            iconLabel = "A",
            colorHex = "#1B5E20",
            sortOrder = 2
        )
    )
}

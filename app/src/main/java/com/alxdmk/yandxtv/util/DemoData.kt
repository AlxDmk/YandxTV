package com.alxdmk.yandxtv.util

import com.alxdmk.yandxtv.domain.model.Site

/**
 * Neutral demo catalogue — three publicly known, openly accessible example sites.
 * No proprietary brands, no piracy, no DRM circumvention.
 */
object DemoData {
    val sites = listOf(
        Site(
            title = "IANA Example Domain",
            url = "https://example.org",
            description = "Стандартный демо-домен IANA, удобный для теста WebView",
            iconLabel = "EX",
            colorHex = "#1565C0",
            sortOrder = 0
        ),
        Site(
            title = "Wikipedia",
            url = "https://ru.wikipedia.org",
            description = "Свободная энциклопедия на русском языке",
            iconLabel = "WP",
            colorHex = "#424242",
            useDesktopUserAgent = true,
            sortOrder = 1
        ),
        Site(
            title = "Hacker News",
            url = "https://news.ycombinator.com",
            description = "Новости и дискуссии из мира IT и стартапов",
            iconLabel = "HN",
            colorHex = "#E65100",
            sortOrder = 2
        )
    )
}

package com.alxdmk.yandxtv.domain.model

data class Site(
    val id: Long = 0L,
    val title: String,
    val url: String,
    val description: String = "",
    val iconLabel: String = "",   // emoji or 1-2 char label as fallback icon
    val colorHex: String = "#1565C0",
    val useDesktopUserAgent: Boolean = false,
    val allowAutofill: Boolean = true,
    val hasCredentials: Boolean = false,
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

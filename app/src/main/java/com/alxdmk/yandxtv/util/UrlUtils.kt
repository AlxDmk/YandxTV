package com.alxdmk.yandxtv.util

import java.net.URI

object UrlUtils {
    fun normalizeUrl(input: String): String? {
        val trimmed = input.trim()
        if (trimmed.isBlank()) return null
        val withScheme = when {
            trimmed.startsWith("http://") || trimmed.startsWith("https://") -> trimmed
            else -> "https://$trimmed"
        }
        return try {
            val uri = URI(withScheme)
            if (uri.host.isNullOrBlank()) null else withScheme
        } catch (e: Exception) { null }
    }

    fun isValidUrl(url: String): Boolean = normalizeUrl(url) != null

    fun extractDomain(url: String): String {
        return try {
            URI(url).host?.removePrefix("www.") ?: url
        } catch (e: Exception) { url }
    }

    fun faviconUrl(siteUrl: String): String {
        val domain = extractDomain(siteUrl)
        return "https://www.google.com/s2/favicons?sz=64&domain=$domain"
    }
}

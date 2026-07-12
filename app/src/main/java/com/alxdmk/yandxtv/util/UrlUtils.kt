package com.alxdmk.yandxtv.util

import android.util.Patterns

object UrlUtils {

    fun normalizeUrl(input: String): String {
        val trimmed = input.trim()
        return when {
            trimmed.startsWith("http://") || trimmed.startsWith("https://") -> trimmed
            trimmed.startsWith("//") -> "https:$trimmed"
            else -> "https://$trimmed"
        }
    }

    fun isValidUrl(url: String): Boolean {
        val normalized = normalizeUrl(url)
        return Patterns.WEB_URL.matcher(normalized).matches()
    }

    fun extractDomain(url: String): String {
        return runCatching {
            val normalized = normalizeUrl(url)
            val host = java.net.URL(normalized).host
            host.removePrefix("www.")
        }.getOrDefault(url.take(30))
    }

    /** Favicon URL via Google's public favicon CDN service. */
    fun faviconUrl(siteUrl: String): String {
        val domain = extractDomain(siteUrl)
        return "https://www.google.com/s2/favicons?domain=$domain&sz=64"
    }

    val DESKTOP_USER_AGENT =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
        "AppleWebKit/537.36 (KHTML, like Gecko) " +
        "Chrome/120.0.0.0 Safari/537.36"

    val MOBILE_USER_AGENT =
        "Mozilla/5.0 (Linux; Android 10; TV) " +
        "AppleWebKit/537.36 (KHTML, like Gecko) " +
        "Chrome/120.0.0.0 Mobile Safari/537.36"
}

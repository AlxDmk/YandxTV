package com.alxdmk.yandxtv.util

import android.util.Patterns
import java.net.URI

object UrlUtils {

    /**
     * Normalizes user input to a valid URL string.
     * Returns null if the URL cannot be normalized.
     *
     * Examples:
     *   "example.com"        -> "https://example.com"
     *   "http://example.com" -> "http://example.com"
     *   "  HTTPS://FOO.BAR " -> "https://foo.bar"
     *   "not a url"          -> null
     */
    fun normalizeUrl(input: String): String? {
        val trimmed = input.trim()
        if (trimmed.isBlank()) return null

        val withScheme = if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            "https://$trimmed"
        } else {
            trimmed
        }

        return if (Patterns.WEB_URL.matcher(withScheme).matches()) {
            withScheme.lowercase()
        } else {
            null
        }
    }

    /**
     * Extracts the host/domain from a URL for display purposes.
     * Returns the original string if parsing fails.
     */
    fun extractDomain(url: String): String {
        return try {
            URI(url).host?.removePrefix("www.") ?: url
        } catch (e: Exception) {
            url
        }
    }

    /**
     * Returns a Google favicon service URL for the given site URL.
     * Uses the size=64 variant for reasonable TV display quality.
     */
    fun faviconUrl(siteUrl: String): String {
        val domain = extractDomain(siteUrl)
        return "https://www.google.com/s2/favicons?domain=$domain&sz=64"
    }

    /**
     * Returns true if the string looks like a valid http/https URL.
     */
    fun isValidUrl(url: String): Boolean = normalizeUrl(url) != null
}

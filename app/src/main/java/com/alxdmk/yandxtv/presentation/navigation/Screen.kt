package com.alxdmk.yandxtv.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object SiteEditor : Screen("site_editor?siteId={siteId}") {
        fun createRoute(siteId: Long? = null) = if (siteId != null) "site_editor?siteId=$siteId" else "site_editor"
        const val ARG_SITE_ID = "siteId"
    }
    object SiteViewer : Screen("site_viewer/{siteId}") {
        fun createRoute(siteId: Long) = "site_viewer/$siteId"
        const val ARG_SITE_ID = "siteId"
    }
    object CredentialEditor : Screen("credential_editor/{siteId}") {
        fun createRoute(siteId: Long) = "credential_editor/$siteId"
        const val ARG_SITE_ID = "siteId"
    }
    object Settings : Screen("settings")
}

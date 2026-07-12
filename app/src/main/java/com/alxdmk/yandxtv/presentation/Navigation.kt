package com.alxdmk.yandxtv.presentation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object EditSite : Screen("edit_site/{siteId}") {
        fun createRoute(siteId: Long = -1L) = "edit_site/$siteId"
    }
    object SiteViewer : Screen("site_viewer/{siteId}") {
        fun createRoute(siteId: Long) = "site_viewer/$siteId"
    }
    object Credentials : Screen("credentials/{siteId}") {
        fun createRoute(siteId: Long) = "credentials/$siteId"
    }
    object Settings : Screen("settings")
}

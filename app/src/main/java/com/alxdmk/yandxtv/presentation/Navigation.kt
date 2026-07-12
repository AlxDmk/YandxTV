package com.alxdmk.yandxtv.presentation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddSite : Screen("add_site")
    object EditSite : Screen("edit_site/{siteId}") {
        fun route(siteId: Long) = "edit_site/$siteId"
    }
    object SiteViewer : Screen("viewer/{siteId}") {
        fun route(siteId: Long) = "viewer/$siteId"
    }
    object Credentials : Screen("credentials/{siteId}") {
        fun route(siteId: Long) = "credentials/$siteId"
    }
    object Settings : Screen("settings")
}

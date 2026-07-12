package com.alxdmk.yandxtv.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alxdmk.yandxtv.presentation.credentials.CredentialsScreen
import com.alxdmk.yandxtv.presentation.edit.EditSiteScreen
import com.alxdmk.yandxtv.presentation.home.HomeScreen
import com.alxdmk.yandxtv.presentation.settings.SettingsScreen
import com.alxdmk.yandxtv.presentation.viewer.SiteViewerScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddSite : Screen("site/add")
    object EditSite : Screen("site/edit/{siteId}") {
        fun createRoute(siteId: Long) = "site/edit/$siteId"
    }
    object SiteViewer : Screen("viewer/{siteId}") {
        fun createRoute(siteId: Long) = "viewer/$siteId"
    }
    object Credentials : Screen("credentials/{siteId}") {
        fun createRoute(siteId: Long) = "credentials/$siteId"
    }
    object Settings : Screen("settings")
}

@Composable
fun YandxTvNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onOpenSite = { siteId -> navController.navigate(Screen.SiteViewer.createRoute(siteId)) },
                onAddSite = { navController.navigate(Screen.AddSite.route) },
                onEditSite = { siteId -> navController.navigate(Screen.EditSite.createRoute(siteId)) },
                onSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.AddSite.route) {
            EditSiteScreen(
                siteId = null,
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditSite.route,
            arguments = listOf(navArgument("siteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val siteId = backStackEntry.arguments?.getLong("siteId")
            EditSiteScreen(
                siteId = siteId,
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.SiteViewer.route,
            arguments = listOf(navArgument("siteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val siteId = backStackEntry.arguments!!.getLong("siteId")
            SiteViewerScreen(
                siteId = siteId,
                onBack = { navController.popBackStack() },
                onSaveCredentials = { id -> navController.navigate(Screen.Credentials.createRoute(id)) }
            )
        }

        composable(
            route = Screen.Credentials.route,
            arguments = listOf(navArgument("siteId") { type = NavType.LongType })
        ) { backStackEntry ->
            val siteId = backStackEntry.arguments!!.getLong("siteId")
            CredentialsScreen(
                siteId = siteId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

package com.alxdmk.yandxtv.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alxdmk.yandxtv.presentation.credentials.CredentialsScreen
import com.alxdmk.yandxtv.presentation.edit.EditSiteScreen
import com.alxdmk.yandxtv.presentation.home.HomeScreen
import com.alxdmk.yandxtv.presentation.settings.SettingsScreen
import com.alxdmk.yandxtv.presentation.settings.SettingsViewModel
import com.alxdmk.yandxtv.presentation.viewer.SiteViewerScreen
import com.alxdmk.yandxtv.ui.theme.YandxTvTheme
import com.alxdmk.yandxtv.domain.model.AppTheme

@Composable
fun YandxTvApp(settingsViewModel: SettingsViewModel = hiltViewModel()) {
    val settings by settingsViewModel.settings.collectAsState()
    val navController = rememberNavController()

    YandxTvTheme(appTheme = settings.theme) {
        NavHost(navController = navController, startDestination = Screen.Home.route) {

            composable(Screen.Home.route) {
                HomeScreen(
                    onOpenSite = { id -> navController.navigate(Screen.SiteViewer.route(id)) },
                    onAddSite = { navController.navigate(Screen.AddSite.route) },
                    onEditSite = { id -> navController.navigate(Screen.EditSite.route(id)) },
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
            ) { backStack ->
                val siteId = backStack.arguments?.getLong("siteId") ?: 0L
                EditSiteScreen(
                    siteId = siteId,
                    onSaved = { navController.popBackStack() },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.SiteViewer.route,
                arguments = listOf(navArgument("siteId") { type = NavType.LongType })
            ) { backStack ->
                val siteId = backStack.arguments?.getLong("siteId") ?: 0L
                SiteViewerScreen(
                    siteId = siteId,
                    onBack = { navController.popBackStack() },
                    onSaveCredentials = { id ->
                        navController.navigate(Screen.Credentials.route(id))
                    }
                )
            }

            composable(
                route = Screen.Credentials.route,
                arguments = listOf(navArgument("siteId") { type = NavType.LongType })
            ) { backStack ->
                val siteId = backStack.arguments?.getLong("siteId") ?: 0L
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
}

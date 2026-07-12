package com.alxdmk.yandxtv.presentation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alxdmk.yandxtv.domain.model.AppTheme
import com.alxdmk.yandxtv.presentation.navigation.Screen
import com.alxdmk.yandxtv.presentation.screens.credential.CredentialEditorScreen
import com.alxdmk.yandxtv.presentation.screens.home.HomeScreen
import com.alxdmk.yandxtv.presentation.screens.settings.SettingsScreen
import com.alxdmk.yandxtv.presentation.screens.siteeditor.SiteEditorScreen
import com.alxdmk.yandxtv.presentation.screens.viewer.SiteViewerScreen
import com.alxdmk.yandxtv.presentation.theme.YandxTvTheme
import com.alxdmk.yandxtv.presentation.viewmodel.SettingsViewModel

@Composable
fun YandxTvApp() {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settings by settingsViewModel.settings.collectAsState()
    val navController = rememberNavController()

    YandxTvTheme(theme = settings.theme) {
        NavHost(navController = navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onOpenSite = { siteId -> navController.navigate(Screen.SiteViewer.createRoute(siteId)) },
                    onAddSite = { navController.navigate(Screen.SiteEditor.createRoute()) },
                    onEditSite = { siteId -> navController.navigate(Screen.SiteEditor.createRoute(siteId)) },
                    onSettings = { navController.navigate(Screen.Settings.route) }
                )
            }
            composable(
                route = Screen.SiteEditor.route,
                arguments = listOf(navArgument(Screen.SiteEditor.ARG_SITE_ID) {
                    type = NavType.StringType; nullable = true; defaultValue = null
                })
            ) { backStack ->
                val siteId = backStack.arguments?.getString(Screen.SiteEditor.ARG_SITE_ID)?.toLongOrNull()
                SiteEditorScreen(siteId = siteId, onNavigateUp = { navController.navigateUp() })
            }
            composable(
                route = Screen.SiteViewer.route,
                arguments = listOf(navArgument(Screen.SiteViewer.ARG_SITE_ID) { type = NavType.LongType })
            ) { backStack ->
                val siteId = backStack.arguments!!.getLong(Screen.SiteViewer.ARG_SITE_ID)
                SiteViewerScreen(
                    siteId = siteId,
                    onNavigateUp = { navController.navigateUp() },
                    onEditCredentials = { navController.navigate(Screen.CredentialEditor.createRoute(siteId)) }
                )
            }
            composable(
                route = Screen.CredentialEditor.route,
                arguments = listOf(navArgument(Screen.CredentialEditor.ARG_SITE_ID) { type = NavType.LongType })
            ) { backStack ->
                val siteId = backStack.arguments!!.getLong(Screen.CredentialEditor.ARG_SITE_ID)
                CredentialEditorScreen(siteId = siteId, onNavigateUp = { navController.navigateUp() })
            }
            composable(Screen.Settings.route) {
                SettingsScreen(onNavigateUp = { navController.navigateUp() })
            }
        }
    }
}

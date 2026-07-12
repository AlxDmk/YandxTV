package com.alxdmk.yandxtv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alxdmk.yandxtv.domain.model.AppTheme
import com.alxdmk.yandxtv.presentation.credentials.CredentialsScreen
import com.alxdmk.yandxtv.presentation.edit.EditSiteScreen
import com.alxdmk.yandxtv.presentation.home.HomeScreen
import com.alxdmk.yandxtv.presentation.settings.SettingsScreen
import com.alxdmk.yandxtv.presentation.settings.SettingsViewModel
import com.alxdmk.yandxtv.presentation.viewer.SiteViewerScreen
import com.alxdmk.yandxtv.ui.theme.YandxTvTheme

@Composable
fun YandxTvApp() {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settings by settingsViewModel.settings.collectAsState()

    val darkTheme = when (settings.theme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.AUTO -> isSystemInDarkTheme()
    }

    YandxTvTheme(darkTheme = darkTheme) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onOpenSite = { siteId ->
                        navController.navigate(Screen.SiteViewer.createRoute(siteId))
                    },
                    onAddSite = {
                        navController.navigate(Screen.EditSite.createRoute())
                    },
                    onEditSite = { siteId ->
                        navController.navigate(Screen.EditSite.createRoute(siteId))
                    },
                    onOpenSettings = {
                        navController.navigate(Screen.Settings.route)
                    }
                )
            }

            composable(
                route = Screen.EditSite.route,
                arguments = listOf(navArgument("siteId") { type = NavType.LongType })
            ) {
                EditSiteScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.SiteViewer.route,
                arguments = listOf(navArgument("siteId") { type = NavType.LongType })
            ) {
                SiteViewerScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onOpenCredentials = { siteId ->
                        navController.navigate(Screen.Credentials.createRoute(siteId))
                    }
                )
            }

            composable(
                route = Screen.Credentials.route,
                arguments = listOf(navArgument("siteId") { type = NavType.LongType })
            ) {
                CredentialsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun isSystemInDarkTheme(): Boolean {
    return androidx.compose.foundation.isSystemInDarkTheme()
}

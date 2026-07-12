package com.alxdmk.yandxtv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.alxdmk.yandxtv.domain.model.AppTheme
import com.alxdmk.yandxtv.domain.repository.SettingsRepository
import com.alxdmk.yandxtv.navigation.YandxTvNavGraph
import com.alxdmk.yandxtv.ui.theme.YandxTvTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settings by settingsRepository.getSettings()
                .collectAsState(initial = runBlocking { settingsRepository.getSettings().first() })

            val darkTheme = when (settings.theme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                AppTheme.AUTO -> isSystemInDarkTheme()
            }

            YandxTvTheme(darkTheme = darkTheme) {
                YandxTvNavGraph()
            }
        }
    }
}

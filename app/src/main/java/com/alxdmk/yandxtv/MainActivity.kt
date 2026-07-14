package com.alxdmk.yandxtv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.alxdmk.yandxtv.data.seed.DemoDataSeeder
import com.alxdmk.yandxtv.domain.repository.SettingsRepository
import com.alxdmk.yandxtv.navigation.YandxTvNavGraph
import com.alxdmk.yandxtv.ui.theme.YandxTvTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var demoDataSeeder: DemoDataSeeder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Сидим предустановленные сайты при первом запуске (внутри сейдера withContext(IO))
        lifecycleScope.launch {
            demoDataSeeder.seedIfEmpty()
        }

        setContent {
            val settings by settingsRepository.getSettings()
                .collectAsState(initial = runBlocking { settingsRepository.getSettings().first() })

            YandxTvTheme(appTheme = settings.theme) {
                YandxTvNavGraph()
            }
        }
    }
}

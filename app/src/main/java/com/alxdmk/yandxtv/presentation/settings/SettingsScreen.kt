package com.alxdmk.yandxtv.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alxdmk.yandxtv.BuildConfig
import com.alxdmk.yandxtv.domain.model.AppTheme

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 2.dp) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                }
                Spacer(Modifier.width(8.dp))
                Text("Настройки", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Theme section
            SettingsSectionHeader("Тема оформления")

            ThemeSelector(
                currentTheme = settings.theme,
                onThemeSelected = viewModel::setTheme
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Browser section
            SettingsSectionHeader("Браузер")

            SettingsToggleRow(
                title = "Desktop User-Agent по умолчанию",
                subtitle = "Открывать все сайты в режиме пк",
                checked = settings.defaultDesktopUa,
                onToggle = viewModel::toggleDefaultDesktopUa
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Install instructions
            SettingsSectionHeader("Установка APK")

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Как установить через флешку:",
                        style = MaterialTheme.typography.titleMedium)
                    listOf(
                        "1. Скопируйте APK-файл на USB-флешку (FAT32)",
                        "2. Вставьте флешку в ТВ",
                        "3. Откройте файловый менеджер (например, ES File Explorer)",
                        "4. Найдите APK-файл и выберите «Установить»",
                        "5. Разрешите установку из неизвестных источников",
                        "6. Приложение появится в лаунчере ТВ"
                    ).forEach { step ->
                        Text(step, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // About
            SettingsSectionHeader("О приложении")

            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("📺 YandxTV", style = MaterialTheme.typography.titleLarge)
                    Text("Версия: ${BuildConfig.VERSION_NAME}",
                        style = MaterialTheme.typography.bodyMedium)
                    Text("Лицензия: MIT",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Каталог сайтов для Android TV. Все данные хранятся локально.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun SettingsToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = { onToggle() })
    }
}

@Composable
private fun ThemeSelector(currentTheme: AppTheme, onThemeSelected: (AppTheme) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AppTheme.values().forEach { theme ->
            val label = when (theme) {
                AppTheme.LIGHT -> "Светлая"
                AppTheme.DARK -> "Тёмная"
                AppTheme.AUTO -> "Авто"
            }
            FilterChip(
                selected = currentTheme == theme,
                onClick = { onThemeSelected(theme) },
                label = { Text(label) }
            )
        }
    }
}

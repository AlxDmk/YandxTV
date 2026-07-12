package com.alxdmk.yandxtv.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.alxdmk.yandxtv.domain.model.Site
import com.alxdmk.yandxtv.util.UrlUtils

@Composable
fun HomeScreen(
    onOpenSite: (Long) -> Unit,
    onAddSite: () -> Unit,
    onEditSite: (Long) -> Unit,
    onSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<Site?>(null) }
    var showExportDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HomeTopBar(
                onSettings = onSettings,
                onExport = { viewModel.exportCatalog(); showExportDialog = true }
            )

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else if (uiState.sites.isEmpty()) {
                EmptyState(onAddSite = onAddSite)
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 200.dp),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    item {
                        AddSiteCard(onClick = onAddSite)
                    }
                    items(uiState.sites, key = { it.id }) { site ->
                        SiteCard(
                            site = site,
                            onOpen = { onOpenSite(site.id) },
                            onEdit = { onEditSite(site.id) },
                            onDelete = { showDeleteDialog = site }
                        )
                    }
                }
            }
        }

        showDeleteDialog?.let { site ->
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("Удалить сайт?") },
                text = { Text("«${site.title}» будет удалён из каталога. Сохранённые учётные данные также будут удалены.") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteSite(site.id)
                        showDeleteDialog = null
                    }) { Text("Удалить", color = MaterialTheme.colorScheme.error) }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = null }) { Text("Отмена") }
                }
            )
        }

        if (showExportDialog && uiState.exportJson != null) {
            ExportDialog(
                json = uiState.exportJson!!,
                onDismiss = { showExportDialog = false; viewModel.clearExport() }
            )
        }
    }
}

@Composable
private fun HomeTopBar(onSettings: () -> Unit, onExport: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 2.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📺 Мой каталог",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onExport) {
                Icon(Icons.Default.Share, contentDescription = "Экспорт")
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onSettings) {
                Icon(Icons.Default.Settings, contentDescription = "Настройки")
            }
        }
    }
}

@Composable
fun SiteCard(
    site: Site,
    onOpen: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Card(
        onClick = onOpen,
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .border(
                width = if (isFocused) 3.dp else 0.dp,
                color = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = MaterialTheme.shapes.medium
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isFocused) 8.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = parseColor(site.colorHex), shape = MaterialTheme.shapes.small),
                    contentAlignment = Alignment.Center
                ) {
                    if (site.iconLabel.isNotBlank()) {
                        Text(text = site.iconLabel.take(2), style = MaterialTheme.typography.labelLarge, color = Color.White)
                    } else {
                        AsyncImage(
                            model = UrlUtils.faviconUrl(site.url),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = site.title, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(text = UrlUtils.extractDomain(site.url), style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                if (site.hasCredentials) {
                    Icon(Icons.Default.Lock, contentDescription = "Есть сохранённые данные",
                        modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                }
            }
            if (site.description.isNotBlank()) {
                Text(text = site.description, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
            if (isFocused) {
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onEdit, contentPadding = PaddingValues(4.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(2.dp))
                        Text("Изменить", style = MaterialTheme.typography.labelSmall)
                    }
                    TextButton(onClick = onDelete, contentPadding = PaddingValues(4.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.width(2.dp))
                        Text("Удалить", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
private fun AddSiteCard(onClick: () -> Unit) {
    var isFocused by remember { mutableStateOf(false) }
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .border(
                width = if (isFocused) 3.dp else 1.dp,
                color = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.medium
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Add, contentDescription = null,
                    modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))
                Text("Добавить сайт", style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun EmptyState(onAddSite: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(Icons.Default.Language, contentDescription = null,
                modifier = Modifier.size(72.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Каталог пуст", style = MaterialTheme.typography.headlineSmall)
            Text("Добавьте первый сайт в каталог", style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Button(onClick = onAddSite, modifier = Modifier.height(52.dp)) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Добавить сайт")
            }
        }
    }
}

@Composable
private fun ExportDialog(json: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Экспорт каталога") },
        text = {
            Column {
                Text("Пароли не включены в экспорт. Скопируйте JSON вручную.",
                    style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Text(json.take(500) + if (json.length > 500) "..." else "",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Закрыть") } }
    )
}

fun parseColor(hex: String): Color = try {
    Color(android.graphics.Color.parseColor(hex))
} catch (e: Exception) {
    Color(0xFF1565C0)
}

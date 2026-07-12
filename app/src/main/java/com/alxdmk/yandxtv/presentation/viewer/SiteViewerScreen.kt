package com.alxdmk.yandxtv.presentation.viewer

import android.webkit.*
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel

const val DESKTOP_UA = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
const val MOBILE_UA = "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"

@Composable
fun SiteViewerScreen(
    siteId: Long,
    onBack: () -> Unit,
    onSaveCredentials: (Long) -> Unit,
    viewModel: SiteViewerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var webView by remember { mutableStateOf<WebView?>(null) }

    LaunchedEffect(siteId) { viewModel.loadSite(siteId) }

    // Handle back from WebView or system
    BackHandler {
        if (webView?.canGoBack() == true) webView?.goBack()
        else onBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Browser toolbar
        Surface(color = MaterialTheme.colorScheme.surface, tonalElevation = 2.dp) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (webView?.canGoBack() == true) webView?.goBack() else onBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }

                    // URL bar
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = uiState.pageTitle.ifBlank { uiState.currentUrl },
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1
                        )
                        Text(
                            text = uiState.currentUrl,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }

                    // Desktop/mobile UA toggle
                    IconButton(onClick = {
                        viewModel.toggleDesktopUa()
                        webView?.settings?.userAgentString =
                            if (!uiState.useDesktopUa) DESKTOP_UA else MOBILE_UA
                        webView?.reload()
                    }) {
                        Icon(
                            if (uiState.useDesktopUa) Icons.Default.Computer else Icons.Default.PhoneAndroid,
                            contentDescription = if (uiState.useDesktopUa) "Desktop UA" else "Mobile UA"
                        )
                    }

                    // Credentials menu
                    if (uiState.hasCredentials) {
                        IconButton(onClick = viewModel::toggleCredentialMenu) {
                            Icon(Icons.Default.Key, contentDescription = "Учётные данные")
                        }
                    } else {
                        IconButton(onClick = { onSaveCredentials(siteId) }) {
                            Icon(Icons.Default.LockOpen, contentDescription = "Сохранить данные")
                        }
                    }
                }

                // Progress indicator
                if (uiState.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // WebView
        Box(modifier = Modifier.weight(1f)) {
            val site = uiState.site
            if (site != null) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            webView = this
                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                loadWithOverviewMode = true
                                useWideViewPort = true
                                setSupportZoom(true)
                                builtInZoomControls = true
                                displayZoomControls = false
                                mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                                userAgentString = if (site.useDesktopUserAgent) DESKTOP_UA else MOBILE_UA
                            }
                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(view: WebView, url: String, favicon: android.graphics.Bitmap?) {
                                    viewModel.onPageStarted(url)
                                }
                                override fun onPageFinished(view: WebView, url: String) {
                                    viewModel.onPageFinished(url, view.title ?: "")
                                }
                            }
                            loadUrl(site.url)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Credentials quick-fill overlay
            if (uiState.showCredentialMenu) {
                CredentialQuickFillMenu(
                    credential = uiState.credential,
                    onFillUsername = { username ->
                        webView?.evaluateJavascript(
                            "document.querySelector('input[type=email],input[type=text],[name*=user],[name*=login],[id*=user],[id*=login]')?.value='$username';",
                            null
                        )
                        viewModel.dismissCredentialMenu()
                    },
                    onFillPassword = { password ->
                        webView?.evaluateJavascript(
                            "document.querySelector('input[type=password]')?.value='$password';",
                            null
                        )
                        viewModel.dismissCredentialMenu()
                    },
                    onDismiss = viewModel::dismissCredentialMenu
                )
            }
        }
    }
}

@Composable
private fun CredentialQuickFillMenu(
    credential: com.alxdmk.yandxtv.domain.model.Credential?,
    onFillUsername: (String) -> Unit,
    onFillPassword: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)),
        contentAlignment = Alignment.TopEnd
    ) {
        Card(
            modifier = Modifier.padding(top = 0.dp, end = 0.dp).width(280.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Быстрый вход", style = MaterialTheme.typography.titleMedium)

                if (!credential?.username.isNullOrBlank()) {
                    OutlinedButton(
                        onClick = { onFillUsername(credential!!.username) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Вставить логин: ${credential!!.username}")
                    }
                }

                if (!credential?.password.isNullOrBlank()) {
                    OutlinedButton(
                        onClick = { onFillPassword(credential!!.password) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Вставить пароль")
                    }
                }

                if (!credential?.note.isNullOrBlank()) {
                    Text("Заметка: ${credential!!.note}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Отмена")
                }
            }
        }
    }
}

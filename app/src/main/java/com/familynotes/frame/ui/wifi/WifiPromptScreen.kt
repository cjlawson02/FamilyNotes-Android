package com.familynotes.frame.ui.wifi

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.sentry.compose.SentryModifier.sentryTag
import io.sentry.compose.SentryTraced

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WifiPromptScreen(
    modifier: Modifier = Modifier,
    onOpenWifiSettings: () -> Unit
) {
    SentryTraced("wifi_prompt_screen") {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Wifi,
                contentDescription = "WiFi",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "WiFi Connection Required",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Please connect to a WiFi network to continue",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = onOpenWifiSettings,
                modifier = Modifier
                    .fillMaxWidth()
                    .sentryTag("button_open_wifi_settings")
            ) {
                Text("Open WiFi Settings")
            }
        }
    }
    }
}


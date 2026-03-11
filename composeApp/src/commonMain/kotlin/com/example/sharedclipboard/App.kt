package com.example.sharedclipboard

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sharedclipboard.auth_ui.AuthScreen
import com.example.sharedclipboard.clipboard_ui.ClipboardScreen
import com.example.sharedclipboard.common_ui.LocalSnackbarHostState
import com.example.sharedclipboard.data.RoomSettings
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    MaterialTheme {
        val snackbarHostState = remember { SnackbarHostState() }

        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                modifier = Modifier.imePadding()
            ) { contentPadding ->
                val roomSettings = koinInject<RoomSettings>()

                var navigateTrigger by remember { mutableStateOf(true) }
                var showAuthScreen by remember(navigateTrigger) {
                    mutableStateOf(roomSettings.roomId == null)
                }

                if (showAuthScreen) {
                    AuthScreen(
                        modifier = Modifier.padding(contentPadding),
                        navigateToClipboardScreen = {
                            navigateTrigger = !navigateTrigger
                        }
                    )
                } else {
                    ClipboardScreen(modifier = Modifier.padding(contentPadding))
                }

            }
        }
    }
}


package com.example.sharedclipboard

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sharedclipboard.auth_ui.AuthJoinCodeScreenStateful
import com.example.sharedclipboard.auth_ui.AuthScreenStateful
import com.example.sharedclipboard.clipboard_ui.ClipboardScreenStateful
import com.example.sharedclipboard.common_ui.LocalSnackbarHostState
import com.example.sharedclipboard.data.RoomSettings
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
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
                    Napier.d { "ROOM ID ${roomSettings.roomId}" }
                    mutableStateOf(roomSettings.roomId == null)
                }
                var showJoinCodeScreen by remember { mutableStateOf(false) }

                if (showAuthScreen) {
                    AuthScreenStateful(
                        modifier = Modifier.padding(contentPadding),
                        navigateToClipboardScreen = {
                            navigateTrigger = !navigateTrigger
                        }
                    )
                } else {
                    if (showJoinCodeScreen) {
                        AuthJoinCodeScreenStateful(
                            modifier = Modifier.padding(contentPadding),
                            onCancel = {
                                showJoinCodeScreen = false
                            },
                        )
                    } else {
                        ClipboardScreenStateful(
                            onGoToAuth = {
                                roomSettings.roomId = null // fixme
                                navigateTrigger = !navigateTrigger
                            },
                            onGoToJoinCode = {
                                showJoinCodeScreen = true
                            },
                            modifier = Modifier.padding(contentPadding)
                        )
                    }
                }

            }
        }
    }
}


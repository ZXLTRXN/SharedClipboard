package com.example.sharedclipboard

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.sharedclipboard.di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier


fun main() {
    Napier.base(DebugAntilog())
    initFirebase()
    initKoin {
        printLogger()
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Shared Clipboard",
            state = rememberWindowState(
                width = 520.dp,
                height = 680.dp
            )
        ) {
            App()
        }
    }
}

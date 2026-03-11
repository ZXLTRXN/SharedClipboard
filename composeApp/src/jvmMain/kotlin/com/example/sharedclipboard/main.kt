package com.example.sharedclipboard

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
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
        ) {
            App()
        }
    }
}
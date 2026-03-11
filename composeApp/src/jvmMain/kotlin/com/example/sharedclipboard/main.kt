package com.example.sharedclipboard

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.sharedclipboard.di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier


fun main() {
    initFirebase()
    initKoin {
        printLogger()
    }
    Napier.base(DebugAntilog())

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Shared Clipboard",
        ) {
            App()
        }
    }
}
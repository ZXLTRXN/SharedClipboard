package com.example.sharedclipboard

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.sharedclipboard.di.initKoin


fun main() {
    initFirebase()
    initKoin {
        printLogger()
    }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "SharedClipboard",
        ) {
            App()
        }
    }
}
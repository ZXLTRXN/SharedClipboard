package com.example.sharedclipboard.clipboard_ui

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface ClipboardRoutes: NavKey {
    @Serializable
    data object Clipboard: ClipboardRoutes
}
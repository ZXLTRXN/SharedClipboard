package com.example.sharedclipboard.clipboard_ui.model

sealed interface ClipboardIntent {
    data class SendLocal(val localClipboard: String) : ClipboardIntent
}
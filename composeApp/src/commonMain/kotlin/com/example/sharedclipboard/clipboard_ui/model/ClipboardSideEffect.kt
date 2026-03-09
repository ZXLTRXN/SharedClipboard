package com.example.sharedclipboard.clipboard_ui.model

interface ClipboardSideEffect {
    data class ShowToast(val text: String)
}

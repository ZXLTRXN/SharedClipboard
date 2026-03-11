package com.example.sharedclipboard.clipboard_ui.state

import org.jetbrains.compose.resources.StringResource

interface ClipboardSideEffect {
    data class ShowSnackbar(val message: StringResource) : ClipboardSideEffect
}

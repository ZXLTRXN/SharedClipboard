package com.example.sharedclipboard.clipboard_ui.model

sealed interface ClipboardState {
    data object Loading : ClipboardState

    data class Success(
        val remoteValue: String,
        val localValue: String,
    ) : ClipboardState

    data object Error : ClipboardState

}
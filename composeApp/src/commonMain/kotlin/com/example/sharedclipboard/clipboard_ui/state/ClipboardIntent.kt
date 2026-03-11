package com.example.sharedclipboard.clipboard_ui.state

sealed interface ClipboardIntent {
    data class SendLocal(val localClipboard: String) : ClipboardIntent
    data object Copied : ClipboardIntent
    data object FailedToOpenUri : ClipboardIntent
    data object goToAuth: ClipboardIntent
    data object goToShowJoinCode: ClipboardIntent
}
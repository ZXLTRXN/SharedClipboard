package com.example.feature.clipboard.ui.state

sealed interface ClipboardIntent {
    data class SendLocal(val localClipboard: String) : ClipboardIntent
    data object Copied : ClipboardIntent
    data object FailedToOpenUri : ClipboardIntent
    data object GoToAuth: ClipboardIntent
    data object GoToShowJoinCode: ClipboardIntent
}
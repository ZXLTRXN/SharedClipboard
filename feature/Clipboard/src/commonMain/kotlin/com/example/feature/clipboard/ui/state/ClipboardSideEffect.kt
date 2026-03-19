package com.example.feature.clipboard.ui.state

import org.jetbrains.compose.resources.StringResource

interface ClipboardSideEffect {
    data class ShowSnackbar(val message: StringResource) : ClipboardSideEffect
    data object GoToAuth : ClipboardSideEffect
}

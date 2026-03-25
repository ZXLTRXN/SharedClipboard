package com.example.feature.clipboard.ui.state

sealed interface ClipboardState {
    data object Loading : ClipboardState

    data class Success(
        val remoteValue: String,
        val localValue: String,
        val remoteLoading: Boolean
    ) : ClipboardState

    data object Error : ClipboardState

}
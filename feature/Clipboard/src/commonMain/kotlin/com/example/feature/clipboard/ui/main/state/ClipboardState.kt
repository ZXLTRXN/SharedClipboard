package com.example.feature.clipboard.ui.main.state

sealed interface ClipboardState {
    data object Loading : ClipboardState

    data class Success(
        val remoteValue: String,
        val localValue: String,
        val remoteLoading: Boolean
    ) : ClipboardState

    data object Error : ClipboardState

}
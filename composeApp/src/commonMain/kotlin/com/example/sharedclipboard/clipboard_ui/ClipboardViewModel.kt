package com.example.sharedclipboard.clipboard_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedclipboard.clipboard_ui.state.ClipboardIntent
import com.example.sharedclipboard.clipboard_ui.state.ClipboardSideEffect
import com.example.sharedclipboard.clipboard_ui.state.ClipboardState
import com.example.sharedclipboard.domain.ClipboardRepository
import com.example.sharedclipboard.domain.LocalClipboardProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sharedclipboard.composeapp.generated.resources.Res
import sharedclipboard.composeapp.generated.resources.copied
import sharedclipboard.composeapp.generated.resources.failedOpenURL

class ClipboardViewModel(
    private val repository: ClipboardRepository,
    localClipboardProvider: LocalClipboardProvider,
    ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    val state = combine(
        repository.observeMessages()
            .onStart { emit("") }
            .flowOn(ioDispatcher),
        localClipboardProvider.currentClipboard
            .onStart { emit("") }
            .flowOn(ioDispatcher),
    ) { remoteValue, localValue ->
        ClipboardState.Success(
            remoteValue,
            localValue
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ClipboardState.Loading
    )

    var sideEffect = Channel<ClipboardSideEffect>(Channel.BUFFERED)
        private set

    fun process(intent: ClipboardIntent) {
        when (intent) {
            is ClipboardIntent.SendLocal -> {
                sendLocal(intent.localClipboard)
            }

            ClipboardIntent.Copied -> sideEffect.trySend(
                ClipboardSideEffect.ShowSnackbar(Res.string.copied)
            )

            ClipboardIntent.FailedToOpenUri -> sideEffect.trySend(
                ClipboardSideEffect.ShowSnackbar(Res.string.failedOpenURL)
            )
        }
    }

    private fun sendLocal(localClipboard: String) {
        viewModelScope.launch {
            repository.saveMessage(localClipboard)
        }
    }
}
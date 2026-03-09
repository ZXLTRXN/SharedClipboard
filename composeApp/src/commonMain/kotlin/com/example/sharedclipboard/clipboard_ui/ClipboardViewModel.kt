package com.example.sharedclipboard.clipboard_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedclipboard.clipboard_ui.model.ClipboardIntent
import com.example.sharedclipboard.clipboard_ui.model.ClipboardSideEffect
import com.example.sharedclipboard.clipboard_ui.model.ClipboardState
import com.example.sharedclipboard.data.ClipboardRepository
import com.example.sharedclipboard.data.LocalClipboardProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    fun process(intent: ClipboardIntent) {
        when (intent) {
            is ClipboardIntent.SendLocal -> {
                sendLocal(intent.localClipboard)
            }
        }
    }

    private fun sendLocal(localClipboard: String) {
        viewModelScope.launch {
            repository.saveMessage(localClipboard)
        }
    }
}
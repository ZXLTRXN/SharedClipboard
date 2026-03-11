package com.example.sharedclipboard.clipboard_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedclipboard.clipboard_ui.state.ClipboardIntent
import com.example.sharedclipboard.clipboard_ui.state.ClipboardSideEffect
import com.example.sharedclipboard.clipboard_ui.state.ClipboardSideEffect.GoToAuth
import com.example.sharedclipboard.clipboard_ui.state.ClipboardSideEffect.ShowSnackbar
import com.example.sharedclipboard.clipboard_ui.state.ClipboardState
import com.example.sharedclipboard.domain.AuthRepository
import com.example.sharedclipboard.domain.ClipboardRepository
import com.example.sharedclipboard.domain.LocalClipboardProvider
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sharedclipboard.composeapp.generated.resources.Res
import sharedclipboard.composeapp.generated.resources.copied
import sharedclipboard.composeapp.generated.resources.errorRelogin
import sharedclipboard.composeapp.generated.resources.failedOpenURL

class ClipboardViewModel(
    private val repository: ClipboardRepository,
    private val authRepository: AuthRepository,
    localClipboardProvider: LocalClipboardProvider,
) : ViewModel() {

    init {
        viewModelScope.launch {
            authRepository.ensureAuth() // fixme
        }
    }

    val state by lazy {
        combine(
            repository.observeMessages()
                .onStart { emit("") },
            localClipboardProvider.currentClipboard
                .onStart { emit("") },
        ) { remoteValue, localValue ->
            ClipboardState.Success(
                remoteValue,
                localValue
            )
        }.catch<ClipboardState> { ex ->
            Napier.e(
                "Cant observe ClipboardViewModel.state",
                ex,
                this@ClipboardViewModel::class.simpleName
            )
            emit(ClipboardState.Error)
        }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                ClipboardState.Loading
            )
    }

    var sideEffect = Channel<ClipboardSideEffect>(Channel.BUFFERED)
        private set

    fun process(intent: ClipboardIntent) {
        when (intent) {
            is ClipboardIntent.SendLocal -> {
                sendLocal(intent.localClipboard)
            }

            ClipboardIntent.Copied -> sideEffect.trySend(
                ShowSnackbar(Res.string.copied)
            )

            ClipboardIntent.FailedToOpenUri -> sideEffect.trySend(
                ShowSnackbar(Res.string.failedOpenURL)
            )

            ClipboardIntent.goToAuth,
            ClipboardIntent.goToShowJoinCode -> {
            }
        }
    }

    private fun sendLocal(localClipboard: String) {
        viewModelScope.launch {
            try {
                repository.saveMessage(localClipboard)
            } catch (ex: IllegalStateException) {
                Napier.e(
                    "Cant save message",
                    ex,
                    this::class.simpleName
                )
                sideEffect.trySend(ShowSnackbar(Res.string.errorRelogin))
                sideEffect.trySend(GoToAuth)
            }
        }
    }
}
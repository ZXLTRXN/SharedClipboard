package com.example.feature.clipboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.clipboard.domain.LocalClipboardProvider
import com.example.feature.clipboard.ui.state.ClipboardIntent
import com.example.feature.clipboard.ui.state.ClipboardSideEffect
import com.example.feature.clipboard.ui.state.ClipboardState
//import io.github.aakira.napier.Napier
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import sharedclipboard.feature.clipboard.generated.resources.Res
import sharedclipboard.feature.clipboard.generated.resources.copied
import sharedclipboard.feature.clipboard.generated.resources.errorRelogin
import sharedclipboard.feature.clipboard.generated.resources.failedOpenURL
import com.example.firebaseapi.domain.AuthRepository
import com.example.firebaseapi.domain.ClipboardRepository

class ClipboardViewModel(
    private val repository: ClipboardRepository,
    private val authRepository: AuthRepository,
    localClipboardProvider: LocalClipboardProvider,
) : ViewModel() {

    private val stateCombination: Flow<ClipboardState> = combine(
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
//        Napier.e( fixme
//            "Caught exception while producing state combination",
//            ex,
//            this@ClipboardViewModel::class.simpleName
//        )
        emit(ClipboardState.Error)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val state = flow {
        val user = authRepository.ensureAuth()
        emit(user)
    }.transformLatest { user ->
        if (user == null) {
//            Napier.e( fixme
//                "User is null",
//                tag = this@ClipboardViewModel::class.simpleName
//            )
            emit(ClipboardState.Error)
        } else {
            emitAll(stateCombination)
        }
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

            ClipboardIntent.GoToAuth,
            ClipboardIntent.GoToShowJoinCode -> {
            }
        }
    }

    private fun sendLocal(localClipboard: String) {
        viewModelScope.launch {
            try {
                repository.saveMessage(localClipboard)
            } catch (ex: IllegalStateException) {
//                Napier.e( fixme
//                    "Cant save message",
//                    ex,
//                    this::class.simpleName
//                )
                sideEffect.trySend(ClipboardSideEffect.ShowSnackbar(Res.string.errorRelogin))
                sideEffect.trySend(ClipboardSideEffect.GoToAuth)
            }
        }
    }
}
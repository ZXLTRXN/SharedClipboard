package com.example.feature.auth.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseapi.domain.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource


class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf<AuthState>(AuthState.Default)
        private set

    var sideEffect = Channel<AuthSideEffect>(Channel.BUFFERED)
        private set

    fun requestJoiningTheRoom(code: String) {
        viewModelScope.launch {
            val result = repository.joinRoom(code)
            result.fold(
                onSuccess = {
                    sideEffect.trySend(AuthSideEffect.GoToClipboardScreen)
                },
                onFailure = {
                    state = AuthState.Error(null)
                }
            )
        }
    }
}

sealed interface AuthState {
    data object Default : AuthState
    data class Error(val message: StringResource?) : AuthState
}

interface AuthSideEffect {
    data object GoToClipboardScreen : AuthSideEffect
}
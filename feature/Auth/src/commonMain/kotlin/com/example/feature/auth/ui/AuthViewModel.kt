package com.example.feature.auth.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.auth.domain.AuthRepository
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
            val isSuccess = repository.joinRoom(code)
            if (!isSuccess) { // fixme
                state = AuthState.Error(null)
            } else {
                sideEffect.trySend(AuthSideEffect.GoToClipboardScreen)
            }
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
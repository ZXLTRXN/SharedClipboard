package com.example.sharedclipboard.auth_ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedclipboard.clipboard_ui.state.ClipboardSideEffect
import com.example.sharedclipboard.domain.AuthRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf<AuthState>(AuthState.Selector)
        private set

    var sideEffect = Channel<AuthSideEffect>(Channel.BUFFERED)
        private set

    init {
        viewModelScope.launch {
            repository.ensureAuth() // fixme
        }
    }

    fun createRoom() {
        viewModelScope.launch {
            repository.createRoom()
            try {
                val code = repository.generateInviteCode()
                state = AuthState.ShowJoinCode(code)
            } catch (ex: Exception) {
                Napier.e("Cant generate code", ex, this::class.simpleName)
                state = AuthState.Error(null)
            }
        }
    }

    fun joinExistingRoom() {
        state = AuthState.JoinExistingRoom
    }

    fun requestJoiningTheRoom(code: String) {
        viewModelScope.launch {
            val isSuccess = repository.joinRoom(code)
            if (!isSuccess) {
                state = AuthState.Error(null)
            } else {
                sideEffect.trySend(AuthSideEffect.GoToMain)
            }
        }
    }

    fun goToSelector() {
        state = AuthState.Selector
    }

}

sealed interface AuthState {
    data object Selector : AuthState
    data class ShowJoinCode(val code: String) : AuthState
    data object JoinExistingRoom : AuthState
    data class Error(val message: StringResource?) : AuthState
}

interface AuthSideEffect {
    data object GoToMain : AuthSideEffect
}
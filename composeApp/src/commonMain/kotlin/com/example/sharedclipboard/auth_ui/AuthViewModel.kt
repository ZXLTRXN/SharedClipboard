package com.example.sharedclipboard.auth_ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sharedclipboard.domain.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var state by mutableStateOf<AuthState>(AuthState.Selector)
        private set

    init {
        viewModelScope.launch {
            repository.ensureAuth()
        }
    }

    fun createRoom() {
        viewModelScope.launch {
            repository.createRoom()
            state = AuthState.ShowJoiningCode(repository.generateInviteCode())
        }
    }

    fun joinExistingRoom() {
        state = AuthState.JoinExistingRoom
    }

    fun requestJoiningTheRoom(code: String) {
        viewModelScope.launch {
            repository.joinRoom(code)
        }
    }

    fun goToSelector() {
        state = AuthState.Selector
    }

}

sealed interface AuthState {
    data object Selector : AuthState
    data class ShowJoiningCode(val code: String) : AuthState
    data object JoinExistingRoom : AuthState
}
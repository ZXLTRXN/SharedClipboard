package com.example.sharedclipboard

import Navigator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseapi.domain.AuthRepository
import kotlinx.coroutines.launch

class AppViewModel(
    private val authRepository: AuthRepository
) : ViewModel(), Navigator.AuthListener {

    init {
        viewModelScope.launch {
            authRepository.ensureAuth()
        }
    }

    val isLoggedIn: Boolean
        get() = authRepository.isRoomAttached

    override fun onLogout() {
        authRepository.quitFromRoom()
    }

    override fun onLogin() {
        authRepository.createRoom()
    }

}
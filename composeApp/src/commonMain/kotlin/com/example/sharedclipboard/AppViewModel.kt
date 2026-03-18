package com.example.sharedclipboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.auth.domain.AuthRepository

import kotlinx.coroutines.launch

class AppViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            authRepository.ensureAuth()
        }
    }

    fun createRoom() {
        authRepository.createRoom()
    }

    fun logout() {
        authRepository.quitFromRoom()
    }

    val isLoggedIn: Boolean
        get() = authRepository.isRoomAttached

}
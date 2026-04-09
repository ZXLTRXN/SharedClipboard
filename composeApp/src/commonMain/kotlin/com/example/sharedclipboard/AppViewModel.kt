package com.example.sharedclipboard

import Navigator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseapi.domain.AuthRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

class AppViewModel(
    private val authRepository: AuthRepository
) : ViewModel(), Navigator.AuthPerformer {

    init {
        viewModelScope.launch {
            authRepository.ensureAuth()
        }
    }

    val isLoggedIn: Boolean
        get() = authRepository.isRoomAttached

    override fun logout() {
        authRepository.quitFromRoom()
    }

    override fun login() {
        if (!isLoggedIn) {
            authRepository.createRoom()
        } else {
            Napier.i("Already logged in", tag = this::class.simpleName)
        }

    }

}
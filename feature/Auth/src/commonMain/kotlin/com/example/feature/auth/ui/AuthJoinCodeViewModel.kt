package com.example.feature.auth.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.auth.domain.AuthRepository
//import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

class AuthJoinCodeViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var state: AuthJoinCodeState by mutableStateOf(AuthJoinCodeState.ShowJoinCode(""))
        private set

    init {
        viewModelScope.launch {
            try {
                state = AuthJoinCodeState.ShowJoinCode(repository.generateInviteCode())
            } catch (ex: Exception) {
//                Napier.e( fixme
//                    "Cant generate code",
//                    ex,
//                    this::class.simpleName
//                )
                state = AuthJoinCodeState.Error(null)
            }
        }
    }
}

sealed interface AuthJoinCodeState {
    data class ShowJoinCode(val code: String) : AuthJoinCodeState
    data class Error(val message: StringResource?) : AuthJoinCodeState
}
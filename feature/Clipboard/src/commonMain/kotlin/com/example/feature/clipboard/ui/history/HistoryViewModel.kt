package com.example.feature.clipboard.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebaseapi.domain.ClipboardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: ClipboardRepository,
) : ViewModel() {

    var list: StateFlow<List<ClipUI>> = repository.allClips()
        .map { list -> list
            .filter { it.text.isNotEmpty() }
            .map { it.toUI() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun delete(clip: ClipUI) {
        viewModelScope.launch {
            repository.deleteClip(clip.timestamp)
        }
    }
}
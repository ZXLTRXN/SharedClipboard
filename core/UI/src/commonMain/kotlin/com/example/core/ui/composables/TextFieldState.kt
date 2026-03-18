package com.example.core.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

// fixme, no need
class TextFieldState(initialText: String = "") {

    var text by mutableStateOf(initialText)
        private set

    fun onTextChange(newText: String) {
        text = newText
    }

    fun clear() {
        text = ""
    }
}


@Composable
fun rememberTextFieldState(initialText: String = ""): TextFieldState {
    return rememberSaveable(saver = InputFieldStateSaver) {
        TextFieldState(initialText)
    }
}

private val InputFieldStateSaver = Saver<TextFieldState, String>(
    save = { it.text },
    restore = { TextFieldState(it) }
)
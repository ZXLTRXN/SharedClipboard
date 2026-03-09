package com.example.sharedclipboard.clipboard_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sharedclipboard.clipboard_ui.model.ClipboardIntent
import com.example.sharedclipboard.clipboard_ui.model.ClipboardState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ClipboardScreen(
    modifier: Modifier = Modifier,
    viewModel: ClipboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ClipboardScreenStateless(
        modifier = modifier,
        state = state,
        onIntent = viewModel::process
    )
}

@Composable
fun ClipboardScreenStateless(
    state: ClipboardState,
    onIntent: (ClipboardIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is ClipboardState.Loading -> {}

        is ClipboardState.Success -> SuccessState(
            state = state,
            onButtonClick = {
                onIntent(it)
            },
            modifier = modifier
        )

        ClipboardState.Error -> {}
    }
}

@Composable
fun SuccessState(
    state: ClipboardState.Success,
    onButtonClick: (ClipboardIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var input by remember(state.localValue) { mutableStateOf(state.localValue) }

        Text(state.remoteValue)
        Text("Local value:")
        Text(state.localValue)
        TextField(
            input,
            onValueChange = { input = it })
        Button(onClick = {
            onButtonClick(
                ClipboardIntent.SendLocal(input)
            )
        }) {
            Text("Send")
        }
    }
}

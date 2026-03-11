package com.example.sharedclipboard.auth_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    navigateToClipboardScreen: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    AuthScreenStateless(
        state = viewModel.state,
        onCreateRoom = viewModel::createRoom,
        onJoinExistingRoom = viewModel::joinExistingRoom,
        onJoinExistingRoomRequest = viewModel::requestJoiningTheRoom,
        onGoToMain = navigateToClipboardScreen,
        onCancel = viewModel::goToSelector,
        modifier = modifier
    )
}

@Composable
fun AuthScreenStateless(
    state: AuthState,
    onCreateRoom: () -> Unit,
    onJoinExistingRoom: () -> Unit,
    onJoinExistingRoomRequest: (String) -> Unit,
    onGoToMain: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val codeInputState = rememberTextFieldState()

    when (state) {
        AuthState.Selector -> {
            AuthSelector(
                onCreateRoom = onCreateRoom,
                onJoinExistingRoom = onJoinExistingRoom,
                modifier = modifier
            )
        }

        is AuthState.ShowJoiningCode -> {
            AuthShowJoiningCode(
                code = state.code,
                onCancel = onCancel, // fixme для
                onGoToMain = onGoToMain,
                modifier = modifier
            )
        }

        is AuthState.JoinExistingRoom -> {
            AuthJoinExistingRoom(
                textFieldState = codeInputState,
                onAccept = onJoinExistingRoomRequest,
                onCancel = onCancel,
                modifier = modifier
            )
        }
    }
}

@Composable
fun AuthJoinExistingRoom(
    textFieldState: TextFieldState,
    onAccept: (String) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier.fillMaxSize().padding(16.dp),
    ) {
        TextButton(
            onClick = onCancel,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Text("cancel")
        }
        TextField(
            state = textFieldState,
            modifier = Modifier.align(Alignment.Center).fillMaxWidth()
        )
        Button(
            onClick = {
                onAccept(textFieldState.text.toString())
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text("connect")
        }
    }
}

@Composable
fun AuthShowJoiningCode(
    code: String,
    modifier: Modifier = Modifier,
    onCancel: (() -> Unit)? = null,
    onGoToMain: (() -> Unit)? = null,

    ) {
    Box(
        modifier.fillMaxSize().padding(16.dp),
    ) {
        onCancel?.let {
            TextButton(
                onClick = it,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Text("cancel")
            }
        }

        onGoToMain?.let {
            TextButton(
                onClick = it,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text("clipboard")
            }
        }

        Text(
            code,
            modifier = Modifier.align(Alignment.Center).fillMaxWidth(),
            textAlign =
                TextAlign.Center
        )
    }
}

@Composable
fun AuthSelector(
    onCreateRoom: () -> Unit,
    onJoinExistingRoom: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedButton(onClick = onCreateRoom) {
            Text("create room")
        }
        Spacer(
            Modifier.height(16.dp)
        )
        OutlinedButton(onClick = onJoinExistingRoom) {
            Text("join room")
        }
    }
}


@Composable
fun AuthScreenPreview() {
    MaterialTheme {
        AuthScreenStateless(
            state = AuthState.Selector,
            onCreateRoom = {},
            onJoinExistingRoom = {},
            onJoinExistingRoomRequest = {},
            onGoToMain = {},
            onCancel = {},
            modifier = Modifier
        )
    }
}
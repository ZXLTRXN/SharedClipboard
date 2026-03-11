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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sharedclipboard.clipboard_ui.state.ClipboardSideEffect
import kotlinx.coroutines.flow.receiveAsFlow
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sharedclipboard.composeapp.generated.resources.Res
import sharedclipboard.composeapp.generated.resources.cancel
import sharedclipboard.composeapp.generated.resources.connect
import sharedclipboard.composeapp.generated.resources.createRoom
import sharedclipboard.composeapp.generated.resources.errorRelogin
import sharedclipboard.composeapp.generated.resources.joinRoom

@Composable
fun AuthScreenStateful(
    navigateToClipboardScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel()
) {

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.receiveAsFlow().collect { effect ->
            when (effect) {
                is AuthSideEffect.GoToMain -> {
                    navigateToClipboardScreen()
                }
            }
        }
    }

    AuthScreen(
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
fun AuthScreen(
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
            AuthSelectorScreen(
                onCreateRoom = onCreateRoom,
                onJoinExistingRoom = onJoinExistingRoom,
                modifier = modifier
            )
        }

        is AuthState.ShowJoinCode -> {
            AuthJoinCodeScreen(
                code = state.code,
                onCancel = onCancel,
                onGoToMain = onGoToMain,
                modifier = modifier
            )
        }

        is AuthState.JoinExistingRoom -> {
            AuthJoinExistingRoomScreen(
                textFieldState = codeInputState,
                onAccept = onJoinExistingRoomRequest,
                onCancel = onCancel,
                modifier = modifier
            )
        }

        is AuthState.Error -> {
            ErrorScreen(
                modifier = modifier,
                goAuth = onCancel
            )
        }
    }
}

@Composable
fun AuthJoinExistingRoomScreen(
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
            Text(stringResource(Res.string.cancel))
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
            Text(stringResource(Res.string.connect))
        }
    }
}

@Composable
fun AuthSelectorScreen(
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
            Text(stringResource(Res.string.createRoom))
        }
        Spacer(
            Modifier.height(16.dp)
        )
        OutlinedButton(onClick = onJoinExistingRoom) {
            Text(stringResource(Res.string.joinRoom))
        }
    }
}


@Composable
@Preview(showBackground = true)
fun AuthScreenPreview() {
    MaterialTheme {
        AuthScreen(
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
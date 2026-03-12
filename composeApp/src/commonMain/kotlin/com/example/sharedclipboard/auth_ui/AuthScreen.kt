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
import com.example.sharedclipboard.common_ui.ErrorScreen
import kotlinx.coroutines.flow.receiveAsFlow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sharedclipboard.composeapp.generated.resources.Res
import sharedclipboard.composeapp.generated.resources.cancel
import sharedclipboard.composeapp.generated.resources.connect
import sharedclipboard.composeapp.generated.resources.createRoom
import sharedclipboard.composeapp.generated.resources.joinRoom

@Composable
fun AuthJoinExistingRoomScreenStateful(
    navigateToClipboardScreen: () -> Unit,
    navigateToAuthScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinViewModel()
) {

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.receiveAsFlow().collect { effect ->
            when (effect) {
                is AuthSideEffect.GoToClipboardScreen -> {
                    navigateToClipboardScreen()
                }
            }
        }
    }

    AuthJoinExistingRoomScreen(
        state = viewModel.state,
        onJoinExistingRoomRequest = viewModel::requestJoiningTheRoom,
        onGoToAuth = navigateToAuthScreen,
        modifier = modifier
    )
}

@Composable
fun AuthJoinExistingRoomScreen(
    state: AuthState,
    onJoinExistingRoomRequest: (String) -> Unit,
    onGoToAuth: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val codeInputState = rememberTextFieldState()

    when (state) {
        is AuthState.Default -> {
            AuthJoinExistingRoomDefaultScreen(
                textFieldState = codeInputState,
                onAccept = onJoinExistingRoomRequest,
                modifier = modifier
            )
        }

        is AuthState.Error -> {
            ErrorScreen(
                modifier = modifier,
                goAuth = onGoToAuth
            )
        }
    }
}

@Composable
fun AuthJoinExistingRoomDefaultScreen(
    textFieldState: TextFieldState,
    onAccept: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier.fillMaxSize().padding(16.dp),
    ) {
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
@Preview(showBackground = true)
fun AuthScreenPreview() {
    MaterialTheme {
        AuthJoinExistingRoomScreen(
            state = AuthState.Default,
            onJoinExistingRoomRequest = {},
            onGoToAuth = {},
            modifier = Modifier
        )
    }
}
package com.example.feature.auth.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.composables.ErrorScreen
import com.example.core.ui.composables.maxWidthButtonsTablets
import kotlinx.coroutines.flow.receiveAsFlow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sharedclipboard.feature.auth.generated.resources.Res
import sharedclipboard.feature.auth.generated.resources.codePlaceholder
import sharedclipboard.feature.auth.generated.resources.connect
import sharedclipboard.feature.auth.generated.resources.enterCode
import sharedclipboard.feature.auth.generated.resources.errorRelogin
import sharedclipboard.feature.auth.generated.resources.toAuth

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
                errorText = Res.string.errorRelogin,
                buttonText = Res.string.toAuth,
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
    val focusManager = LocalFocusManager.current
    Box(
        modifier.fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }.padding(16.dp),
    ) {
        TextField(
            state = textFieldState,
            label = {
                Text(stringResource(Res.string.enterCode))
            },
            placeholder = {
                Text(stringResource(Res.string.codePlaceholder))
            },
            modifier = Modifier.align(Alignment.Center)
                .widthIn(max = maxWidthButtonsTablets)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )
        Button(
            onClick = {
                onAccept(textFieldState.text.toString())
            },
            modifier = Modifier.align(Alignment.BottomCenter)
                .widthIn(max = maxWidthButtonsTablets)
                .fillMaxWidth()

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
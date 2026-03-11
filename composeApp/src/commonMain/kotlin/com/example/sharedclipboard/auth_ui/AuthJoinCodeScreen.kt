package com.example.sharedclipboard.auth_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sharedclipboard.composeapp.generated.resources.Res
import sharedclipboard.composeapp.generated.resources.cancel
import sharedclipboard.composeapp.generated.resources.clipboard
import sharedclipboard.composeapp.generated.resources.errorRelogin

@Composable
fun AuthJoinCodeScreenStateful(
    modifier: Modifier = Modifier,
    viewModel: AuthJoinCodeViewModel = koinViewModel(),
    onCancel: (() -> Unit)? = null,
    onGoToMain: (() -> Unit)? = null,
) {
    when(val state = viewModel.state) {
        is AuthJoinCodeState.ShowJoinCode -> {
            AuthJoinCodeScreen(
                code = state.code,
                modifier = modifier,
                onCancel = onCancel,
                onGoToMain = onGoToMain
            )
        }

        is AuthJoinCodeState.Error -> {
            AuthJoinCodeScreen(
                code = stringResource(Res.string.errorRelogin),
                modifier = modifier,
                onCancel = onCancel,
                onGoToMain = onGoToMain
            )
        }
    }

}

@Composable
fun AuthJoinCodeScreen(
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
                Text(stringResource(Res.string.cancel))
            }
        }

        onGoToMain?.let {
            TextButton(
                onClick = it,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(stringResource(Res.string.clipboard))
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
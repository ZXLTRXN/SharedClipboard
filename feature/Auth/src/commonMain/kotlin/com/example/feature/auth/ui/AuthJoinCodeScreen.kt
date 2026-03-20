package com.example.feature.auth.ui

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
import sharedclipboard.feature.auth.generated.resources.Res
import sharedclipboard.feature.auth.generated.resources.clipboard
import sharedclipboard.feature.auth.generated.resources.errorRelogin

@Composable
fun AuthJoinCodeScreenStateful(
    modifier: Modifier = Modifier,
    viewModel: AuthJoinCodeViewModel = koinViewModel(),

    onGoToMain: (() -> Unit)? = null,
) {
    when(val state = viewModel.state) {
        is AuthJoinCodeState.ShowJoinCode -> {
            AuthJoinCodeScreen(
                code = state.code,
                modifier = modifier,
                onGoToMain = onGoToMain
            )
        }

        is AuthJoinCodeState.Error -> {
            AuthJoinCodeScreen(
                code = stringResource(Res.string.errorRelogin),
                modifier = modifier,
                onGoToMain = onGoToMain
            )
        }
    }

}

@Composable
fun AuthJoinCodeScreen(
    code: String,
    modifier: Modifier = Modifier,
    onGoToMain: (() -> Unit)? = null,
) {
    Box(
        modifier
            .padding(vertical = 24.dp, horizontal = 16.dp),
    ) {

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
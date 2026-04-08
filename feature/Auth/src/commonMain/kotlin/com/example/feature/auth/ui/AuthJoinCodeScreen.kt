package com.example.feature.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sharedclipboard.feature.auth.generated.resources.Res
import sharedclipboard.feature.auth.generated.resources.copyCode
import sharedclipboard.feature.auth.generated.resources.errorRelogin

@Composable
fun AuthJoinCodeScreenStateful(
    modifier: Modifier = Modifier,
    viewModel: AuthJoinCodeViewModel = koinViewModel(),
) {
    when (val state = viewModel.state) {
        is AuthJoinCodeState.ShowJoinCode -> {
            AuthJoinCodeScreen(
                code = state.code,
                modifier = modifier
            )
        }

        is AuthJoinCodeState.Error -> {
            AuthJoinCodeScreen(
                code = stringResource(Res.string.errorRelogin),
                modifier = modifier
            )
        }
    }

}

@Composable
fun AuthJoinCodeScreen(
    code: String,
    modifier: Modifier = Modifier,

    ) {
    Column(
        modifier = modifier
            .padding(
                vertical = 24.dp,
                horizontal = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(Res.string.copyCode),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
            )

        Text(
            code,
            modifier = Modifier,
            fontWeight = FontWeight.Bold,
        )
    }
}
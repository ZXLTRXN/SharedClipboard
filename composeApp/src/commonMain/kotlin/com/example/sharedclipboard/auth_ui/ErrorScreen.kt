package com.example.sharedclipboard.auth_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import sharedclipboard.composeapp.generated.resources.Res
import sharedclipboard.composeapp.generated.resources.errorRelogin
import sharedclipboard.composeapp.generated.resources.toAuth

@Composable
fun ErrorScreen(
    error: StringResource = Res.string.errorRelogin,
    goAuth: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(stringResource(error))

        TextButton(onClick = goAuth, modifier = Modifier.align(Alignment.TopStart)) {
            Text(stringResource(Res.string.toAuth))
        }
    }
}
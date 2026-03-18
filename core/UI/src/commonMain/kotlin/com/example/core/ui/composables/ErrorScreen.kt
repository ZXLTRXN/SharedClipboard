package com.example.sharedclipboard.common_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorScreen(
    errorText: StringResource,
    buttonText: StringResource,
    goAuth: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(stringResource(errorText))

        TextButton(onClick = goAuth, modifier = Modifier.align(Alignment.TopStart)) {
            Text(stringResource(buttonText))
        }
    }
}
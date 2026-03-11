package com.example.sharedclipboard.clipboard_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sharedclipboard.clipboard_ui.state.ClipboardIntent
import com.example.sharedclipboard.clipboard_ui.state.ClipboardSideEffect
import com.example.sharedclipboard.clipboard_ui.state.ClipboardState
import com.example.sharedclipboard.common_ui.FlashTextWithDetection
import com.example.sharedclipboard.common_ui.ReloadableTextField
import com.example.sharedclipboard.common_ui.LocalSnackbarHostState
import kotlinx.coroutines.flow.receiveAsFlow
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import sharedclipboard.composeapp.generated.resources.Res
import sharedclipboard.composeapp.generated.resources.emptyBuffer
import sharedclipboard.composeapp.generated.resources.localClipboard
import sharedclipboard.composeapp.generated.resources.send

@Composable
fun ClipboardScreen(
    modifier: Modifier = Modifier,
    viewModel: ClipboardViewModel = koinViewModel()
) {

    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.receiveAsFlow().collect { effect ->
            when (effect) {
                is ClipboardSideEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = getString(effect.message)
                    )
                }
            }
        }
    }

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
            onIntent = onIntent,
            modifier = modifier
        )

        ClipboardState.Error -> {}
    }
}

@Composable
fun SuccessState(
    state: ClipboardState.Success,
    onIntent: (ClipboardIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val clipboard = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier.fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var reloadTrigger by remember { mutableStateOf(false) }

        var input by remember(
            state.localValue,
            reloadTrigger
        ) { mutableStateOf(state.localValue) }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(
                    scrollState,
                )
        ) {

            if (state.remoteValue.isBlank()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(Res.string.emptyBuffer),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                FlashTextWithDetection(
                    state.remoteValue,
                    modifier = Modifier.fillMaxWidth(),
                    onLongPress = {
                        clipboard.setText(AnnotatedString(state.remoteValue))
                        onIntent(
                            ClipboardIntent.Copied
                        )
                    },
                    onDoubleTap = {
                        try {
                            uriHandler.openUri(state.remoteValue)
                        } catch (ex: Exception) {
                            onIntent(
                                ClipboardIntent.FailedToOpenUri
                            )
                        }
                    })
            }


            Spacer(modifier = Modifier.height(30.dp))
            ReloadableTextField(
                input,
                onValueChange = { input = it },
                onReload = { reloadTrigger = !reloadTrigger },
                labelRes = Res.string.localClipboard,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onIntent(
                    ClipboardIntent.SendLocal(input)
                )
            }) {
            Text(stringResource(Res.string.send))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClipboardScreenPreview() {
    MaterialTheme {
        ClipboardScreenStateless(
            state = ClipboardState.Success(
                localValue = "local",
                remoteValue = "remote"
            ),
            onIntent = {}
        )
    }
}

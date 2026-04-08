package com.example.feature.clipboard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.core.ui.composables.ErrorScreen
import com.example.core.ui.composables.FlashTextWithDetection
import com.example.core.ui.composables.LoadingScreen
import com.example.core.ui.composables.LocalSnackbarHostState
import com.example.core.ui.composables.TwoTrailingTextField
import com.example.feature.clipboard.ui.state.ClipboardIntent
import com.example.feature.clipboard.ui.state.ClipboardSideEffect
import com.example.feature.clipboard.ui.state.ClipboardState
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import sharedclipboard.feature.clipboard.generated.resources.Res
import sharedclipboard.feature.clipboard.generated.resources.emptyBuffer
import sharedclipboard.feature.clipboard.generated.resources.errorRelogin
import sharedclipboard.feature.clipboard.generated.resources.localClipboard
import sharedclipboard.feature.clipboard.generated.resources.send
import sharedclipboard.feature.clipboard.generated.resources.share_ic
import sharedclipboard.feature.clipboard.generated.resources.refresh_ic
import sharedclipboard.feature.clipboard.generated.resources.link_ic
import sharedclipboard.feature.clipboard.generated.resources.toAuth

@Composable
fun ClipboardScreenStateful(
    modifier: Modifier = Modifier,
    viewModel: ClipboardViewModel = koinViewModel(),
    onGoToAuth: () -> Unit,
    onGoToJoinCode: () -> Unit
) {

    val snackbarHostState = LocalSnackbarHostState.current

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { effect ->
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
    ClipboardScreen(
        modifier = modifier,
        state = state,
        onIntent = { intent ->
            when (intent) {
                is ClipboardIntent.SendLocal,
                ClipboardIntent.FailedToOpenUri,
                ClipboardIntent.Copied -> viewModel.process(intent)

                ClipboardIntent.GoToAuth -> onGoToAuth.invoke()
                ClipboardIntent.GoToShowJoinCode -> onGoToJoinCode()
            }
        }
    )
}

@Composable
fun ClipboardScreen(
    state: ClipboardState,
    onIntent: (ClipboardIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is ClipboardState.Loading -> {
            LoadingScreen()
        }

        is ClipboardState.Success -> ClipboardSuccessStateScreen(
            state = state,
            onIntent = onIntent,
            modifier = modifier
        )

        ClipboardState.Error -> {
            ErrorScreen(
                errorText = Res.string.errorRelogin,
                buttonText = Res.string.toAuth,
                modifier = modifier,
                goAuth = {
                    onIntent(ClipboardIntent.GoToAuth)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ClipboardSuccessStateScreen(
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
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        var reloadTrigger by remember { mutableStateOf(false) }

        var input by rememberSaveable(
            state.localValue,
            reloadTrigger
        ) { mutableStateOf(state.localValue) }

        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier.fillMaxWidth()
        ) {

            IconButton(onClick = {
                onIntent(
                    ClipboardIntent.GoToShowJoinCode
                )
            }) {
                Icon(vectorResource(Res.drawable.share_ic), contentDescription = null)
            }
        }


        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(
                    scrollState,
                )
        ) {

            when {
                state.remoteLoading -> {
                    LoadingIndicator(modifier = Modifier.fillMaxWidth())
                }

                state.remoteValue.isBlank() -> {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(Res.string.emptyBuffer),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {
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
            }

            Spacer(modifier = Modifier.height(30.dp))
            TwoTrailingTextField(
                input,
                onValueChange = { input = it },
                labelRes = Res.string.localClipboard,
                firstIcon = Res.drawable.link_ic,
                secondIcon = Res.drawable.refresh_ic,
                onFirstClick = {
                    val filtered = InputFilters.filterContent(input)
                    if (filtered != input) {
                        input = filtered
                    }
                },
                onSecondClick = { reloadTrigger = !reloadTrigger },
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
        ClipboardScreen(
            state = ClipboardState.Success(
                localValue = "local",
                remoteValue = "remote",
                remoteLoading = false
            ),
            onIntent = {}
        )
    }
}

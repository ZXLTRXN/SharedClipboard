package com.example.sharedclipboard

import Navigator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.AppBackground
import com.example.core.ui.SharedClipboardTheme
import com.example.core.ui.composables.LocalSnackbarHostState
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import rememberNavigator
import routes.AuthRoutes
import routes.ClipboardRoutes
import routes.navSerializersConfig

@Composable
@Preview
fun App() {
    SharedClipboardTheme {
        val snackbarHostState = remember { SnackbarHostState() }

        val appViewModel: AppViewModel = koinViewModel()

        val navigator = rememberNavigator(
            navSerializersConfig,
            appViewModel.isLoggedIn
        )

        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
            AppBackground {
                Scaffold(
                    containerColor = Color.Transparent,
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    bottomBar = {
                        AnimatedVisibility(
                            visible = navigator.showBottomBar,
                            enter = slideInVertically(
                                initialOffsetY = { fullHeight -> fullHeight },
                                animationSpec = tween(durationMillis = 300)
                            ) + fadeIn(),
                            exit = slideOutVertically(
                                targetOffsetY = { fullHeight -> fullHeight },
                                animationSpec = tween(durationMillis = 300)
                            ) + fadeOut()
                        ) {
                            BottomBar(
                                navigator = navigator,
                                modifier = Modifier
                            )
                        }
                    }
                ) { contentPadding ->
                    AppNavDisplay(
                        appViewModel = appViewModel,
                        navigator = navigator,
                        contentPadding
                    )
                }
            }
        }
    }
}

@Composable
fun BottomBar(
    navigator: Navigator,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f),
                    RoundedCornerShape(28.dp),
                ),
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
            tonalElevation = 6.dp,
        ) {
            NavigationBarItem(
                selected = navigator.topLevelRoute == AuthRoutes.SelectMethod,
                onClick = { navigator.clearAndGoTo(AuthRoutes.SelectMethod) },
                icon = {
                    Icon(
                        vectorResource(AuthRoutes.SelectMethod.icon),
                        contentDescription = null
                    )
                }
            )

            NavigationBarItem(
                selected = navigator.topLevelRoute == ClipboardRoutes.Clipboard,
                onClick = { navigator.clearAndGoTo(ClipboardRoutes.Clipboard) },
                icon = {
                    Icon(
                        vectorResource(ClipboardRoutes.Clipboard.icon),
                        contentDescription = null
                    )
                }
            )
        }
    }
}

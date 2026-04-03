package com.example.sharedclipboard

import Navigator
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
    MaterialTheme {
        val snackbarHostState = remember { SnackbarHostState() }

        val appViewModel: AppViewModel = koinViewModel()

        val navigator = rememberNavigator(
            navSerializersConfig,
            appViewModel.isLoggedIn
        )

        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                modifier = Modifier.imePadding(),
                bottomBar = {
                    if (navigator.isLoggedIn) {
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

@Composable
fun BottomBar(
    navigator: Navigator,
    modifier: Modifier = Modifier
) {
    BottomAppBar(modifier = modifier) {
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


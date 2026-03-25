package com.example.sharedclipboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.feature.auth.ui.authFeature
import com.example.feature.clipboard.ui.clipboardFeature
import org.koin.compose.viewmodel.koinViewModel
import rememberNavigator
import routes.AuthRoutes
import routes.ClipboardRoutes
import routes.navSerializersConfig


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavDisplay(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel = koinViewModel()
) {
    val defaultRoute = if (appViewModel.isLoggedIn) {
        ClipboardRoutes.Clipboard
    } else {
        AuthRoutes.SelectMethod
    }

    val navigator = rememberNavigator(
        navSerializersConfig,
        defaultRoute,
    )

    val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }

    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.goBack() },
        sceneStrategy = bottomSheetStrategy,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),

        entryProvider = entryProvider {
            clipboardFeature(
                navigator,
                modifier,
                contentPadding
            )
            authFeature(
                navigator,
                modifier,
                contentPadding,
                appViewModel::createRoom
            )
        }
    )
}
package com.example.sharedclipboard

import BottomSheetSceneStrategy
import Navigator
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavDisplay(
    appViewModel: AppViewModel,
    navigator: Navigator,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {

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
                navigator = navigator,
                onLogout = {
                    navigator.logout(appViewModel)
                },
                modifier = modifier,
                contentPadding = contentPadding,
                createRoom =  {
                    navigator.login(appViewModel)
                }
            )
        }
    )
}
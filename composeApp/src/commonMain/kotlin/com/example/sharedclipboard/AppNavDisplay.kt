package com.example.sharedclipboard

import AuthRoutes
import ClipboardRoutes
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
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.feature.auth.ui.authFeature
import com.example.feature.clipboard.ui.clipboardFeature
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel
import rememberNavigator


@OptIn(ExperimentalSerializationApi::class)
private val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclassesOfSealed<AuthRoutes>()
            subclassesOfSealed<ClipboardRoutes>()
        }
    }
}

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
        config,
        defaultRoute
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
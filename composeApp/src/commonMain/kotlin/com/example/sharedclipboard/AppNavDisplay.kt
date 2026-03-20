package com.example.sharedclipboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.example.feature.auth.ui.AuthJoinCodeScreenStateful
import com.example.feature.auth.ui.AuthJoinExistingRoomScreenStateful
import com.example.feature.auth.ui.AuthRoutes
import com.example.feature.auth.ui.AuthSelectorScreen
import com.example.feature.clipboard.ui.ClipboardRoutes
import com.example.feature.clipboard.ui.ClipboardScreenStateful
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

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
    val backStack = rememberNavBackStack(
        config,
        defaultRoute
    )

    val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        sceneStrategy = bottomSheetStrategy,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<ClipboardRoutes.Clipboard> {
                ClipboardScreenStateful(
                    onGoToAuth = {
                        appViewModel.logout()
                        backStack.removeLastOrNull()
                        backStack.add(AuthRoutes.SelectMethod)
                    },
                    onGoToJoinCode = {
                        backStack.add(AuthRoutes.ShowJoinCode)
                    },
                    modifier = modifier.padding(contentPadding)
                )
            }
            entry<AuthRoutes.SelectMethod> {
                AuthSelectorScreen(
                    onCreateRoom = {
                        appViewModel.createRoom()
                        backStack.removeLastOrNull()
                        backStack.add(ClipboardRoutes.Clipboard)
                        backStack.add(AuthRoutes.ShowJoinCode)
                    },
                    onJoinExistingRoom = {
                        backStack.add(AuthRoutes.JoinExistingRoom)
                    },
                    modifier = Modifier.padding(contentPadding)
                )
            }
            entry<AuthRoutes.ShowJoinCode>(
                metadata = BottomSheetSceneStrategy.bottomSheet()
            ) {
                AuthJoinCodeScreenStateful(
                    modifier = Modifier.wrapContentHeight(),
                )
            }
            entry<AuthRoutes.JoinExistingRoom> {
                AuthJoinExistingRoomScreenStateful(
                    modifier = Modifier.padding(contentPadding),
                    navigateToClipboardScreen = {
                        backStack.removeLastOrNull()
                        backStack.add(ClipboardRoutes.Clipboard)
                    },
                    navigateToAuthScreen = {
                        backStack.removeLastOrNull()
                        backStack.add(AuthRoutes.SelectMethod)
                    }
                )
            }

        }
    )
}
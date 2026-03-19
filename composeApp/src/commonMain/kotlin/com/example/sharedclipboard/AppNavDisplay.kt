package com.example.sharedclipboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
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

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { key ->
            when (key) {
                is ClipboardRoutes.Clipboard -> NavEntry(key) {
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

                is AuthRoutes.SelectMethod -> NavEntry(key) {
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

                is AuthRoutes.ShowJoinCode -> NavEntry(key) {
                    AuthJoinCodeScreenStateful(
                        modifier = Modifier.padding(contentPadding),
                    )
                }

                is AuthRoutes.JoinExistingRoom -> NavEntry(key) {
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

                else -> {
                    error("Unknown route: $key")
                }
            }
        }
    )
}
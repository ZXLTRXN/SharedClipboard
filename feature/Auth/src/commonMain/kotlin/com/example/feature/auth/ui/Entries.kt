package com.example.feature.auth.ui

import Navigator
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import routes.AuthRoutes
import routes.ClipboardRoutes

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.authFeature(
    navigator: Navigator,
    onLogout: () -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {

    entry<AuthRoutes.SelectMethod> {
        AuthSelectorScreen(
            isLoggedIn = navigator.isLoggedIn,
            onLeaveRoom = {
                onLogout()
            },
            onCreateRoom = {
                onLogout()
                onLogin()
                navigator.clearAndGoTo(ClipboardRoutes.Clipboard)
                navigator.goTo(AuthRoutes.ShowJoinCode)
            },
            onJoinExistingRoom = {
                navigator.goTo(AuthRoutes.JoinExistingRoom)
            },
            modifier = modifier.padding(contentPadding)
        )
    }
    entry<AuthRoutes.ShowJoinCode>(
        metadata = BottomSheetSceneStrategy.bottomSheet()
    ) {
        AuthJoinCodeScreenStateful(
            modifier = modifier.wrapContentHeight(),
        )
    }
    entry<AuthRoutes.JoinExistingRoom> {
        AuthJoinExistingRoomScreenStateful(
            modifier = modifier.padding(contentPadding),
            navigateToClipboardScreen = {
                onLogin() // room already created in AuthViewModel
                navigator.clearAndGoTo(ClipboardRoutes.Clipboard)
            },
            navigateToAuthScreen = {
                navigator.clearAndGoTo(AuthRoutes.SelectMethod)
            }
        )
    }
}
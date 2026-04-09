package com.example.sharedclipboard

import BottomSheetSceneStrategy
import Navigator
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
    val animationDuration = 300
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
                onLogin = {
                    navigator.login(appViewModel)
                },
                modifier = modifier,
                contentPadding = contentPadding
            )
        },
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(animationDuration)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(animationDuration)
            )
        },
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(animationDuration)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(animationDuration)
            )
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(animationDuration)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(animationDuration)
            )
        }
    )
}
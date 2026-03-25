package com.example.feature.clipboard.ui

import Navigator
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import routes.AuthRoutes
import routes.ClipboardRoutes

fun EntryProviderScope<NavKey>.clipboardFeature(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues
) {
    entry<ClipboardRoutes.Clipboard> {
        ClipboardScreenStateful(
            onGoToAuth = {
                navigator.clearAndGoTo(AuthRoutes.SelectMethod)
            },
            onGoToJoinCode = {
                navigator.goTo(AuthRoutes.ShowJoinCode)
            },
            modifier = modifier.padding(contentPadding)
        )
    }
}
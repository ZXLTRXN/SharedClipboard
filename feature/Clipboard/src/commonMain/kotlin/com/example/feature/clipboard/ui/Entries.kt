package com.example.feature.clipboard.ui

import Navigator
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.feature.clipboard.ui.history.HistoryScreenStateful
import com.example.feature.clipboard.ui.main.ClipboardScreenStateful
import routes.AuthRoutes
import routes.ClipboardRoutes

@OptIn(ExperimentalMaterial3Api::class)
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
            onGoToHistory = {
                navigator.goTo(ClipboardRoutes.History)
            },
            modifier = modifier.padding(contentPadding)
                .consumeWindowInsets(contentPadding)
            // нужно чтобы корректно сработал ime внутри (windowInsetsPadding(ime))
        )
    }

    entry<ClipboardRoutes.History>(
    ) {
        HistoryScreenStateful(
            contentPadding = contentPadding,
            modifier = modifier
        )
    }
}
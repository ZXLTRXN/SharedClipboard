package com.example.sharedclipboard.data


import com.example.feature.clipboard.domain.LocalClipboardProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIPasteboard

class IosClipboardProvider(
    ioDispatcher: CoroutineDispatcher
): LocalClipboardProvider {

    override val currentClipboard: Flow<String> = callbackFlow {
        val pasteboard = UIPasteboard.generalPasteboard

        val updateClipboard = {
            val text = pasteboard.string ?: ""
            trySend(text)
        }

        val observer = NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue
        ) { _ ->
            updateClipboard()
        }

        updateClipboard()

        awaitClose {
            NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
    }.distinctUntilChanged()
        .flowOn(ioDispatcher)
}


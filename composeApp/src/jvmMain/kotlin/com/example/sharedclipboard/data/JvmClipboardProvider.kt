package com.example.sharedclipboard.data

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class JvmClipboardProvider : LocalClipboardProvider {

    private val clipboard = Toolkit.getDefaultToolkit().systemClipboard

    override val currentClipboard: Flow<String> = callbackFlow {
        while (isActive) {
            val text = try {
                if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                    clipboard.getData(DataFlavor.stringFlavor) as String
                } else ""
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }

            trySend(text)

            delay(1_000)
        }
    }.distinctUntilChanged()
}
package com.example.sharedclipboard.data

import com.example.feature.clipboard.domain.LocalClipboardProvider
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor

class JvmClipboardProvider(
    ioDispatcher: CoroutineDispatcher
): LocalClipboardProvider {

    private val clipboard = Toolkit.getDefaultToolkit().systemClipboard

    override val currentClipboard: Flow<String> = flow {
        var lastText: String? = null
        var errorReported = false

        while (currentCoroutineContext().isActive) {
            try {
                val text = readClipboardText()
                errorReported = false

                if (text != lastText) {
                    lastText = text
                    emit(text)
                }
            } catch (exception: Exception) {
                // The system clipboard can be temporarily unavailable. Keep the last
                // successful value and avoid producing a stack trace on every poll.
                if (!errorReported) {
                    Napier.e(
                        message = "Failed to read the system clipboard",
                        throwable = exception,
                        tag = this@JvmClipboardProvider::class.simpleName
                    )
                    errorReported = true
                }
            }

            delay(POLL_INTERVAL_MILLIS)
        }
    }.flowOn(ioDispatcher)

    private fun readClipboardText(): String =
        if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
            clipboard.getData(DataFlavor.stringFlavor) as? String ?: ""
        } else {
            ""
        }

    private companion object {
        const val POLL_INTERVAL_MILLIS = 2_000L
    }
}

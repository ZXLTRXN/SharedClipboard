package com.example.sharedclipboard.data

import android.content.ClipboardManager
import android.content.Context
import com.example.feature.clipboard.domain.LocalClipboardProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

class AndroidClipboardProvider(
    context: Context,
    ioDispatcher: CoroutineDispatcher
) : LocalClipboardProvider {

    private val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    override val currentClipboard: Flow<String> = callbackFlow {
        val listener = ClipboardManager.OnPrimaryClipChangedListener {
            val text = getStringOrEmpty()
            trySend(text)
        }

        clipboardManager.addPrimaryClipChangedListener(listener)

        val initialText = getStringOrEmpty()
        trySend(initialText)

        awaitClose {
            clipboardManager.removePrimaryClipChangedListener(listener)
        }
    }.distinctUntilChanged()
        .flowOn(ioDispatcher)

    private fun getStringOrEmpty(): String {
        return try {
            clipboardManager.primaryClip?.getItemAt(0)?.text?.toString().orEmpty()
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }
}
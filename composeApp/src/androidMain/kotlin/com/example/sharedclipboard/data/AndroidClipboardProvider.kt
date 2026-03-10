package com.example.sharedclipboard.data

import android.content.ClipboardManager
import android.content.Context
import com.example.sharedclipboard.domain.LocalClipboardProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class AndroidClipboardProvider(
    context: Context
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

    private fun getStringOrEmpty(): String {
        return try {
            clipboardManager.primaryClip?.getItemAt(0)?.text?.toString().orEmpty()
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }
    }
}
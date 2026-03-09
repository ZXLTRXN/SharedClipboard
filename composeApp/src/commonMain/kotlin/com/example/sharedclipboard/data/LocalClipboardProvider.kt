package com.example.sharedclipboard.data

import kotlinx.coroutines.flow.Flow

interface LocalClipboardProvider {
    val currentClipboard: Flow<String>
}
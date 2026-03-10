package com.example.sharedclipboard.domain

import kotlinx.coroutines.flow.Flow

interface LocalClipboardProvider {
    val currentClipboard: Flow<String>
}
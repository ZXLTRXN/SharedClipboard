package com.example.feature.clipboard.domain

import kotlinx.coroutines.flow.Flow

interface LocalClipboardProvider {
    val currentClipboard: Flow<String>
}
package com.example.sharedclipboard

import kotlinx.coroutines.flow.Flow

interface ClipboardRepository {
    suspend fun saveMessage(text: String)
    fun observeMessages(): Flow<String>
}
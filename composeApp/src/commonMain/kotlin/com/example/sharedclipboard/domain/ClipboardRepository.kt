package com.example.sharedclipboard.domain

import kotlinx.coroutines.flow.Flow

interface ClipboardRepository {
    suspend fun saveMessage(text: String)
    fun observeMessages(): Flow<String>
}
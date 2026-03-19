package com.example.feature.clipboard.domain

import kotlinx.coroutines.flow.Flow

interface ClipboardRepository {
    /**
     * @throws IllegalStateException if no room saved
     */
    suspend fun saveMessage(text: String)

    /**
     * @throws IllegalStateException in flow if no room saved
     */
    fun observeMessages(): Flow<String>
}
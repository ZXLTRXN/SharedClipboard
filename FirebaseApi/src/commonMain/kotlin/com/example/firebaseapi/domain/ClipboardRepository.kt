package com.example.firebaseapi.domain

import kotlinx.coroutines.flow.Flow

interface ClipboardRepository {
    /**
     * @throws NoAttachedRoomException if no room saved
     */
    suspend fun saveMessage(text: String)

    /**
     * @throws NoAttachedRoomException in flow if no room saved
     */
    fun observeMessages(): Flow<ClipModel>
}
package com.example.firebaseapi.domain

import kotlinx.coroutines.flow.Flow

interface ClipboardRepository {
    /**
     * @throws NoAttachedRoomException if no room saved
     */
    suspend fun saveClip(text: String)

    suspend fun deleteClip(timestamp: Long)

    /**
     * @throws NoAttachedRoomException in flow if no room saved
     */
    fun latestClip(): Flow<ClipModel>
    fun allClips(): Flow<List<ClipModel>>
}
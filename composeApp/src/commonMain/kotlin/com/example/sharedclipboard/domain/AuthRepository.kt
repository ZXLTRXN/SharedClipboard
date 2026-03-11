package com.example.sharedclipboard.domain

interface AuthRepository {

    suspend fun ensureAuth()

    suspend fun createRoom()

    /**
     * @throws IllegalStateException if no room attached
     */
    suspend fun generateInviteCode(expiresInMsec: Long = 5 * 60 * 1000L): String

    suspend fun joinRoom(code: String): Boolean
}
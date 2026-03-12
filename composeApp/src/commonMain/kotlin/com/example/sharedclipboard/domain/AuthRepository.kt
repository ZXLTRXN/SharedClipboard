package com.example.sharedclipboard.domain

import dev.gitlive.firebase.auth.FirebaseUser

interface AuthRepository {

    val isRoomAttached: Boolean

    suspend fun ensureAuth(): FirebaseUser?

    fun createRoom()

    fun quitFromRoom()


    /**
     * @throws IllegalStateException if no room attached
     */
    suspend fun generateInviteCode(expiresInMsec: Long = 5 * 60 * 1000L): String

    suspend fun joinRoom(code: String): Boolean
}
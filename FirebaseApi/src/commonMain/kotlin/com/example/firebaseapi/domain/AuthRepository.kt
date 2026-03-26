package com.example.firebaseapi.domain

import dev.gitlive.firebase.auth.FirebaseUser

interface AuthRepository {

    val isRoomAttached: Boolean

    suspend fun ensureAuth(): FirebaseUser?

    fun createRoom()

    fun quitFromRoom()


    /**
     * @throws NoAttachedRoomException if no room attached
     */
    suspend fun generateInviteCode(expiresInMsec: Long = 5 * 60 * 1000L): String

    suspend fun joinRoom(code: String): Result<Unit>
}
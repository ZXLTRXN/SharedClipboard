package com.example.sharedclipboard.data

import com.example.sharedclipboard.data.models.ClipboardDataDto
import com.example.sharedclipboard.data.models.InviteDto
import com.example.sharedclipboard.domain.AuthRepository
import com.example.sharedclipboard.domain.ClipboardRepository
import com.example.sharedclipboard.getPlatform
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.DatabaseReference
import dev.gitlive.firebase.database.FirebaseDatabase
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.time.Clock

class FirebaseRepository(
    database: FirebaseDatabase,
    private val auth: FirebaseAuth,
    private val settings: RoomSettings,
    private val ioDispatcher: CoroutineDispatcher
) : ClipboardRepository, AuthRepository {

    private val roomsRef = database.reference("rooms")
    private val invitesRef = database.reference("invites")

    override suspend fun ensureAuth() {
        if (auth.currentUser == null) {
            auth.signInAnonymously()
        }
    }

    override suspend fun createRoom() {
        val newRoomId = Random.nextLong().toString()
        settings.roomId = newRoomId
    }

    /**
     * @throws IllegalStateException if no room saved
     */
    override suspend fun generateInviteCode(expiresInMsec: Long): String =
        withContext(ioDispatcher) {
            val roomId = settings.roomId
                ?: throw IllegalStateException("No saved roomId")

            val code = (100000..999999).random().toString()
            val invite = InviteDto(
                roomId = roomId,
                expiresAt = Clock.System.now().toEpochMilliseconds() + expiresInMsec
            )

            invitesRef.child(code).setValue(invite)
            return@withContext code
        }

    override suspend fun joinRoom(code: String): Boolean = withContext(ioDispatcher) {
        try {
            val snapshot = invitesRef.child(code).valueEvents.first()

            if (snapshot.exists) {
                val invite = snapshot.value<InviteDto>()
                val now = Clock.System.now().toEpochMilliseconds()

                if (invite.expiresAt > now) {

                    settings.roomId = invite.roomId

                    invitesRef.child(code).removeValue()
                    return@withContext true
                }
            }
        } catch (e: Exception) {
            Napier.e(
                "Cant join the room",
                e,
                this::class.simpleName
            )
        }
        return@withContext false
    }

    private fun getLastClipSnapshot(roomId: String): DatabaseReference =
        roomsRef.child(roomId).child("last_clip")

    /**
     * @throws IllegalStateException if no room saved
     */
    override suspend fun saveMessage(text: String) = withContext(ioDispatcher) {
        val roomId = settings.roomId ?: throw IllegalStateException("No saved roomId")
        val deviceId = auth.currentUser?.uid ?: "unknown"

        val newMessage = ClipboardDataDto(
            text = text,
            timestamp = Clock.System.now().toEpochMilliseconds(),
            senderId = deviceId,
            senderName = getPlatform().name
        )

        getLastClipSnapshot(roomId).setValue(newMessage)
    }

    /**
     * @throws IllegalStateException if no room saved
     */
    override fun observeMessages(): Flow<String> {
        val roomId = settings.roomId ?: throw IllegalStateException("No saved roomId")

        return getLastClipSnapshot(roomId).valueEvents.mapNotNull { snapshot ->
            snapshot.value<ClipboardDataDto?>()?.text
        }
    }
}
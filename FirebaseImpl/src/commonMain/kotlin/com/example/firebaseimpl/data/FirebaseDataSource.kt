package com.example.firebaseimpl.data

import com.example.firebaseimpl.data.models.ClipboardDataDto
import com.example.firebaseimpl.data.models.InviteDto
import dev.gitlive.firebase.database.DatabaseReference
import dev.gitlive.firebase.database.FirebaseDatabase
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull
import io.mockative.Mockable
import kotlinx.coroutines.flow.flow

@Mockable
internal interface FirebaseDataSource {
    suspend fun saveClip(
        roomId: String,
        dto: ClipboardDataDto
    )

    fun getClips(roomId: String): Flow<ClipboardDataDto>

    suspend fun saveInvite(
        code: String,
        dto: InviteDto
    )

    suspend fun getInvite(code: String): InviteDto?

    suspend fun removeCode(code: String): Unit

}

internal class FirebaseDataSourceImpl(
    database: FirebaseDatabase,
) : FirebaseDataSource {

    private val roomsRef = database.reference("rooms")
    private val invitesRef = database.reference("invites")

    private fun getLastClipSnapshot(roomId: String): DatabaseReference =
        roomsRef.child(roomId).child("last_clip")

    override suspend fun saveClip(
        roomId: String,
        dto: ClipboardDataDto
    ) {
        try {
            getLastClipSnapshot(roomId).setValue(dto)
        } catch (ex: Exception) {
            Napier.e("saveClip failed", ex, this::class.simpleName)
        }
    }

    override fun getClips(roomId: String): Flow<ClipboardDataDto> {
        return try {
            getLastClipSnapshot(roomId).valueEvents
                .mapNotNull { it.value<ClipboardDataDto?>() }
        } catch (ex: Exception) {
            Napier.e("getClips failed", ex, this::class.simpleName)
            flow {
                throw ex
            }
        }

    }

    override suspend fun saveInvite(
        code: String,
        dto: InviteDto
    ) {
        try {
            invitesRef.child(code).setValue(dto)
        } catch (ex: Exception) {
            Napier.e("saveInvite failed", ex, this::class.simpleName)
        }
    }

    override suspend fun getInvite(code: String): InviteDto? {
        val snapshot = invitesRef.child(code).valueEvents.firstOrNull()
        return try {
            snapshot?.value<InviteDto>()
        } catch (ex: Exception) {
            Napier.e("getInvite failed", ex, this::class.simpleName)
            null
        }
    }

    override suspend fun removeCode(code: String) {
        try {
            invitesRef.child(code).removeValue()
        } catch (ex: Exception) {
            Napier.e("removeCode failed", ex, this::class.simpleName)
        }
    }


}
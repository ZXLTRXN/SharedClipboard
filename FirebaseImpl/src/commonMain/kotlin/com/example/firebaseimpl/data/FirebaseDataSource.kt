package com.example.firebaseimpl.data

import com.example.firebaseimpl.data.models.ClipboardDataDto
import com.example.firebaseimpl.data.models.InviteDto
import dev.gitlive.firebase.database.DatabaseReference
import dev.gitlive.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull
import io.mockative.Mockable

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
        getLastClipSnapshot(roomId).setValue(dto)
    }

    override fun getClips(roomId: String): Flow<ClipboardDataDto> {
        return getLastClipSnapshot(roomId).valueEvents
            .mapNotNull { it.value<ClipboardDataDto?>() }
    }

    override suspend fun saveInvite(
        code: String,
        dto: InviteDto
    ) {
        invitesRef.child(code).setValue(dto)
    }

    override suspend fun getInvite(code: String): InviteDto? {
        val snapshot = invitesRef.child(code).valueEvents.firstOrNull()
        return snapshot?.value<InviteDto>()
    }

    override suspend fun removeCode(code: String) {
        invitesRef.child(code).removeValue()
    }


}
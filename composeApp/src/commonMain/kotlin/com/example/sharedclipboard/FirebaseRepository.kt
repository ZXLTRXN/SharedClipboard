package com.example.sharedclipboard

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlin.time.Clock

class FirebaseRepository(
//    private val database: FirebaseDatabase fixme
) : ClipboardRepository {
    private val database =
        Firebase.database("https://shared-clipboard-bc736-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val clipboardsRef = database.reference("clipboards")

    override suspend fun saveMessage(text: String) {
        val newMessage = ClipboardDataDto(
            text = text,
            timestamp = Clock.System.now().toEpochMilliseconds(),
            senderId = "user123"
        )

        clipboardsRef.setValue(newMessage)
    }

    override fun observeMessages(): Flow<String> {
        return clipboardsRef.valueEvents.mapNotNull { snapshot ->
            snapshot.value<ClipboardDataDto?>()?.text
        }
    }
}
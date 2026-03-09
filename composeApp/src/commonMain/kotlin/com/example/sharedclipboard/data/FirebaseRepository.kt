package com.example.sharedclipboard.data

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import kotlin.time.Clock

class FirebaseRepository(
//    private val database: FirebaseDatabase fixme
    private val ioDispatcher: CoroutineDispatcher
) : ClipboardRepository {
    private val database =
        Firebase.database("https://shared-clipboard-bc736-default-rtdb.asia-southeast1" +
                ".firebasedatabase.app/") // fixme
    private val clipboardsRef = database.reference("clipboards")

    override suspend fun saveMessage(text: String) = withContext(ioDispatcher) { // fixme
        val newMessage = ClipboardDataDto(
            text = text,
            timestamp = Clock.System.now().toEpochMilliseconds(),
            senderId = "" // fixme
        )

        clipboardsRef.setValue(newMessage)
    }

    override fun observeMessages(): Flow<String> {
        return clipboardsRef.valueEvents.mapNotNull { snapshot ->
            snapshot.value<ClipboardDataDto?>()?.text
        }
    }
}
package com.example.sharedclipboard.data

import com.example.sharedclipboard.domain.ClipboardRepository
import com.example.sharedclipboard.getPlatform
import dev.gitlive.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import kotlin.time.Clock

class FirebaseRepository(
    database: FirebaseDatabase,
    private val ioDispatcher: CoroutineDispatcher
) : ClipboardRepository {
    private val clipboardsRef = database.reference("clipboards")

    override suspend fun saveMessage(text: String) = withContext(ioDispatcher) {
        val newMessage = ClipboardDataDto(
            text = text,
            timestamp = Clock.System.now().toEpochMilliseconds(),
            senderId = getPlatform().name
        )

        clipboardsRef.setValue(newMessage)
    }

    override fun observeMessages(): Flow<String> {
        return clipboardsRef.valueEvents.mapNotNull { snapshot ->
            snapshot.value<ClipboardDataDto?>()?.text
        }
    }
}
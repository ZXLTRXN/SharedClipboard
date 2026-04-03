package com.example.firebaseimpl.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneNotNull
import app.cash.sqldelight.db.SqlDriver
import com.example.core.cache.db.ClipboardQueries
import com.example.firebaseapi.domain.AuthRepository
import com.example.firebaseapi.domain.ClipModel
import com.example.firebaseapi.domain.ClipboardRepository
import com.example.firebaseapi.domain.CodeNotFoundException
import com.example.firebaseapi.domain.ExpiredException
import com.example.firebaseapi.domain.NoAttachedRoomException
import com.example.firebaseimpl.data.models.ClipboardDataDto
import com.example.firebaseimpl.data.models.InviteDto
import com.example.firebaseimpl.platform
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.database.DatabaseException
import io.github.aakira.napier.Napier
import io.mockative.Mockable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.random.nextULong
import kotlin.time.Clock


@Mockable(Clock::class)
internal class FirebaseRepository(
    private val dataSource: FirebaseDataSource,
    private val auth: FirebaseAuthAdapter,
    private val settings: RoomSettings,
    private val clipboardCache: ClipboardQueries,
    private val ioDispatcher: CoroutineDispatcher,
    private val clock: Clock
) : ClipboardRepository, AuthRepository {

    override val isRoomAttached: Boolean
        get() = settings.roomId != null

    override suspend fun ensureAuth(): FirebaseUser? {
        return try {
            if (auth.currentUser == null) {
                auth.signInAnonymously()
            }
            auth.currentUser
        } catch (ex: Exception) {
            Napier.e("ensureAuth failed", ex, this::class.simpleName)
            null
        }

    }

    override fun createRoom() {
        val newRoomId = Random.nextULong().toString()
        settings.roomId = newRoomId
    }

    override fun quitFromRoom() {
        settings.roomId = null
    }

    /**
     * @throws IllegalStateException if no room saved
     */
    override suspend fun generateInviteCode(expiresInMsec: Long): String =
        withContext(ioDispatcher) {
            val roomId = settings.roomId
                ?: throw IllegalStateException("No saved roomId")

            val code = RANGE.random().toString()
            val invite = InviteDto(
                roomId = roomId,
                expiresAt = clock.now().toEpochMilliseconds() + expiresInMsec
            )

            dataSource.saveInvite(
                code,
                invite
            )
            return@withContext code
        }

    override suspend fun joinRoom(code: String): Result<Unit> = withContext(ioDispatcher) {
        val invite =
            dataSource.getInvite(code) ?: return@withContext Result.failure(CodeNotFoundException())

        val now = clock.now().toEpochMilliseconds()

        if (invite.expiresAt > now) {
            settings.roomId = invite.roomId

            dataSource.removeCode(code)
            return@withContext Result.success(Unit)
        } else {
            dataSource.removeCode(code)
            return@withContext Result.failure(ExpiredException())
        }

    }

    /**
     * @throws NoAttachedRoomException if no room saved
     */
    override suspend fun saveMessage(text: String) = withContext(ioDispatcher) {
        val roomId: String = settings.roomId ?: throw NoAttachedRoomException()
        val deviceId: String = auth.currentUser?.uid ?: "unknown"

        val newMessage = ClipboardDataDto(
            text = text,
            timestamp = clock.now().toEpochMilliseconds(),
            senderId = deviceId,
            senderName = platform()
        )
        dataSource.saveClip(
            roomId,
            newMessage
        )
    }

    /**
     * @throws NoAttachedRoomException IN FLOW if no room saved
     */
    override fun observeMessages(): Flow<ClipModel> = channelFlow {
        val roomId = settings.roomId ?: throw NoAttachedRoomException()

        dataSource.getClips(roomId)
            .onEach { dto ->
                clipboardCache.insertClip(
                    timestamp = dto.timestamp,
                    text = dto.text,
                    room_id = roomId,
                    sender_id = dto.senderId,
                    sender_name = dto.senderName
                )
            }
            .launchIn(this@channelFlow)

        clipboardCache.selectLatestClip(roomId)
            .asFlow()
            .mapToOneNotNull(ioDispatcher)
            .collect { entity ->
                send(
                    ClipModel(
                        timestamp = entity.timestamp,
                        text = entity.text,
                        senderName = entity.sender_name
                    )
                )
            }
    }.flowOn(ioDispatcher)

    companion object {
        private val RANGE = 100000..999999
    }
}
package com.example.firebaseimpl

import app.cash.turbine.test
import com.example.core.cache.db.ClipboardQueries
import com.example.core.cache.db.Database
import com.example.firebaseapi.domain.NoAttachedRoomException
import com.example.firebaseimpl.data.FirebaseAuthAdapter
import com.example.firebaseimpl.data.FirebaseDataSource
import com.example.firebaseimpl.data.FirebaseRepository
import com.example.firebaseimpl.data.RoomSettings
import com.example.firebaseimpl.data.models.ClipboardDataDto
import io.mockative.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class FirebaseRepositoryTest : KoinTest {

    private val repository: FirebaseRepository by inject()
    private val clipboardCache: ClipboardQueries by inject()

    private val dataSource = mock(of<FirebaseDataSource>())
    private val auth = mock(of<FirebaseAuthAdapter>())
    private val settings = mock(of<RoomSettings>())
    private val clock = mock(of<Clock>())
    private val testDispatcher = StandardTestDispatcher()


    private val testModule = module {
        single { dataSource }
        single { auth }
        single { settings }
        single { clock }
        single<CoroutineDispatcher> { testDispatcher }

        single {
            val driver = createTestSqlDriver()
            Database.Schema.create(driver)
            Database(driver).clipboardQueries
        }

        single {
            FirebaseRepository(
                dataSource = get(),
                auth = get(),
                settings = get(),
                clipboardCache = get(),
                ioDispatcher = get(),
                clock = get()
            )
        }
    }

    @BeforeTest
    fun setup() {
        startKoin {
            modules(testModule)
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `generateInviteCode should save invite when roomId is present`() = runTest(testDispatcher) {
        // Given
        val fixedNow = Instant.fromEpochMilliseconds(1000000)
        val roomId = "test-room-id"
        val expiresIn = 60000L


        every { settings.roomId }.returns(roomId)
        every { clock.now() }.returns(fixedNow)

        // When
        val code = repository.generateInviteCode(expiresIn)

        // Then
        assertTrue(code.toInt() in 100000..999999)

        coVerify {
            dataSource.saveInvite(
                eq(code),
                matches { it.roomId == roomId && it.expiresAt == 1060000L }
            )
        }.wasInvoked()
    }

    @Test
    fun `observeMessages should cache remote clips and emit from local cache`() =
        runTest(testDispatcher) {

            // 2. Given
            val roomId = "room123"
            val remoteDto = ClipboardDataDto(
                text = "Hello from Firebase",
                timestamp = 1000L,
                senderId = "user_1",
                senderName = "Android Device"
            )

            every { settings.roomId }.returns(roomId)

            every { dataSource.getClips(roomId) }.returns(flowOf(remoteDto))

            // 3. When & Then
            repository.observeMessages().test {
                val item = awaitItem()

                assertEquals(
                    "Hello from Firebase",
                    item.text
                )
                assertEquals(
                    "Android Device",
                    item.senderName
                )
                assertEquals(
                    1000L,
                    item.timestamp
                )

                val dbContent = clipboardCache.selectLatestClip().executeAsOne()
                assertEquals(
                    "Hello from Firebase",
                    dbContent.text
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `saveMessage throws NoAttachedRoomException when roomId is null`() =
        runTest(testDispatcher) {
            // Given
            every { settings.roomId }.returns(null)

            // When & Then
            assertFailsWith<NoAttachedRoomException> {
                repository.saveMessage("Hello World")
            }
        }
}
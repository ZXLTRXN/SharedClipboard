package com.example.firebaseimpl.di

import com.example.datautils.IoQualifier
import com.example.firebaseapi.domain.AuthRepository
import com.example.firebaseapi.domain.ClipboardRepository
import com.example.firebaseimpl.data.FirebaseAuthAdapter
import com.example.firebaseimpl.data.FirebaseAuthAdapterImpl
import com.example.firebaseimpl.data.FirebaseDataSource
import com.example.firebaseimpl.data.FirebaseDataSourceImpl
import com.example.firebaseimpl.data.FirebaseRepository
import com.example.firebaseimpl.data.RoomSettings
import com.russhwolf.settings.Settings
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.database
import org.koin.dsl.binds
import org.koin.dsl.module
import kotlin.time.Clock

val firebaseModule = module {
    single<Settings> { Settings() }
    factory { RoomSettings(get()) }

    single<FirebaseDatabase> {
        Firebase.database(
            "https://shared-clipboard-bc736-default-rtdb.asia-southeast1" +
                    ".firebasedatabase.app/"
        )
    }

    single<FirebaseDataSource> {
        FirebaseDataSourceImpl(get())
    }

    single<FirebaseAuth> {
        Firebase.auth
    }

    single<FirebaseAuthAdapter> {
        FirebaseAuthAdapterImpl(get())
    }

    factory<Clock> {
        Clock.System
    }

    single {
        FirebaseRepository(
            get(),
            get(),
            get(),
            get(),
            get(IoQualifier),
            get()
        )
    } binds arrayOf(
        ClipboardRepository::class,
        AuthRepository::class,
    )
}
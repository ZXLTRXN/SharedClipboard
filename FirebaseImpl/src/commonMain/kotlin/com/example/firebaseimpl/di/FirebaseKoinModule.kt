package com.example.firebaseimpl.di

import com.example.datautils.IoQualifier
import com.example.firebaseapi.domain.AuthRepository
import com.example.firebaseapi.domain.ClipboardRepository
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

val firebaseModule = module {
    single<Settings> { Settings() }
    factory { RoomSettings(get()) }

    single<FirebaseDatabase> {
        Firebase.database(
            "https://shared-clipboard-bc736-default-rtdb.asia-southeast1" +
                    ".firebasedatabase.app/"
        )
    }
    single<FirebaseAuth> {
        Firebase.auth
    }

    single {
        FirebaseRepository(
            get(),
            get(),
            get(),
            get(),
            get(IoQualifier)
        )
    } binds arrayOf(
        ClipboardRepository::class,
        AuthRepository::class,
    )
}
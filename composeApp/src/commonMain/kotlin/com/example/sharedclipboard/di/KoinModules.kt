package com.example.sharedclipboard.di

import com.example.feature.auth.domain.AuthRepository
import com.example.sharedclipboard.AppViewModel
import com.example.sharedclipboard.clipboard_ui.ClipboardViewModel
import com.example.sharedclipboard.data.FirebaseRepository
import com.example.sharedclipboard.data.RoomSettings
import com.example.sharedclipboard.domain.ClipboardRepository
import com.russhwolf.settings.Settings
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.binds
import org.koin.dsl.module


val sharedModule = module {

    factory<CoroutineDispatcher> { Dispatchers.Default }

    factory<CoroutineDispatcher>(IoQualifier) { Dispatchers.IO }

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
            get(IoQualifier)
        )
    } binds arrayOf(
        ClipboardRepository::class,
        AuthRepository::class
    )

    viewModel<AppViewModel> {
        AppViewModel(
            get()
        )
    }

    viewModel<ClipboardViewModel> {
        ClipboardViewModel(
            get(),
            get(),
            get()
        )
    }
}

object IoQualifier : Qualifier {
    override val value: String = "Io"
}


expect val platformModule: Module
package com.example.sharedclipboard.di

import com.example.sharedclipboard.clipboard_ui.ClipboardViewModel
import com.example.sharedclipboard.data.ClipboardRepository
import com.example.sharedclipboard.data.FirebaseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module


val sharedModule = module {

    factory<CoroutineDispatcher> { Dispatchers.Default }

    factory<CoroutineDispatcher>(IoQualifier) { Dispatchers.IO }

    single<ClipboardRepository> {
        FirebaseRepository(get(IoQualifier))
    }

    viewModel<ClipboardViewModel> {
        ClipboardViewModel(
            get(),
            get(),
            get(IoQualifier)
        )
    }
}

object IoQualifier : Qualifier {
    override val value: String = "Io"
}


expect val platformModule: Module
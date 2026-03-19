package com.example.sharedclipboard.di

import com.example.datautils.IoQualifier
import com.example.sharedclipboard.AppViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val sharedModule = module {

    factory<CoroutineDispatcher> { Dispatchers.Default }

    factory<CoroutineDispatcher>(IoQualifier) { Dispatchers.IO }



    viewModel<AppViewModel> {
        AppViewModel(
            get()
        )
    }
}


expect val platformModule: Module
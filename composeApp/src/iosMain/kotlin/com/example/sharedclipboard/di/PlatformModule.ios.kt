package com.example.sharedclipboard.di

import com.example.sharedclipboard.data.IosClipboardProvider
import com.example.sharedclipboard.domain.LocalClipboardProvider
import org.koin.dsl.module

actual val platformModule = module {
    single<LocalClipboardProvider> { IosClipboardProvider(get(IoQualifier)) }
}
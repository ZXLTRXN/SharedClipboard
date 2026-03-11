package com.example.sharedclipboard.di

import com.example.sharedclipboard.data.AndroidClipboardProvider
import com.example.sharedclipboard.domain.LocalClipboardProvider
import org.koin.dsl.module

actual val platformModule = module {
    single<LocalClipboardProvider> { AndroidClipboardProvider(get(), get(IoQualifier)) }
}
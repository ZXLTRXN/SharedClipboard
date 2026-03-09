package com.example.sharedclipboard.di

import com.example.sharedclipboard.data.AndroidClipboardProvider
import com.example.sharedclipboard.data.LocalClipboardProvider
import org.koin.dsl.module

actual val platformModule = module {
    single<LocalClipboardProvider> { AndroidClipboardProvider(get()) }
}
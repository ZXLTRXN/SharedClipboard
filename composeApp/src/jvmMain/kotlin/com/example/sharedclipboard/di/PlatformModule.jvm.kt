package com.example.sharedclipboard.di

import com.example.sharedclipboard.data.JvmClipboardProvider
import com.example.sharedclipboard.domain.LocalClipboardProvider
import org.koin.dsl.module

actual val platformModule = module {
        single<LocalClipboardProvider> { JvmClipboardProvider() }
}
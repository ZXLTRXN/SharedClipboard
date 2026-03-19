package com.example.sharedclipboard.di

import com.example.datautils.IoQualifier
import com.example.feature.clipboard.domain.LocalClipboardProvider
import com.example.sharedclipboard.data.AndroidClipboardProvider
import org.koin.dsl.module

actual val platformModule = module {
    single<LocalClipboardProvider> { AndroidClipboardProvider(get(), get(IoQualifier)) }
}
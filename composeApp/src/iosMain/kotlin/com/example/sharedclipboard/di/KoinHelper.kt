package com.example.sharedclipboard.di

import com.example.feature.clipboard.ui.main.ClipboardViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class KoinHelper: KoinComponent {
    fun getClipboardViewModel(): ClipboardViewModel {
        return get()
    }
}
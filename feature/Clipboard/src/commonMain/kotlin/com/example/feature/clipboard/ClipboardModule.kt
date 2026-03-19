package com.example.feature.clipboard

import com.example.feature.clipboard.ui.ClipboardViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val clipboardModule = module {
    viewModel<ClipboardViewModel> {
        ClipboardViewModel(
            get(),
            get(),
            get()
        )
    }
}
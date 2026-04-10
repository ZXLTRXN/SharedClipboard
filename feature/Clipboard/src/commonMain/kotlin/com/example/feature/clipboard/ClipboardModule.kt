package com.example.feature.clipboard

import com.example.feature.clipboard.ui.history.HistoryViewModel
import com.example.feature.clipboard.ui.main.ClipboardViewModel
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
    viewModel<HistoryViewModel> {
        HistoryViewModel(
            get(),
        )
    }
}
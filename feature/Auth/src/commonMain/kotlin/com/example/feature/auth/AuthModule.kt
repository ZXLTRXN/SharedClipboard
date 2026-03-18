package com.example.feature.auth

import com.example.feature.auth.ui.AuthJoinCodeViewModel
import com.example.feature.auth.ui.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    viewModel<AuthViewModel> {
        AuthViewModel(
            get()
        )
    }
    viewModel<AuthJoinCodeViewModel> {
        AuthJoinCodeViewModel(
            get()
        )
    }
}
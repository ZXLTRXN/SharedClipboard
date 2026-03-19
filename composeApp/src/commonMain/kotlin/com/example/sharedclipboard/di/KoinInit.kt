package com.example.sharedclipboard.di

import com.example.feature.auth.authModule
import com.example.feature.clipboard.clipboardModule
import com.example.firebaseimpl.di.firebaseModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

fun initKoin(config: KoinAppDeclaration? = null): KoinApplication {
    return startKoin {
        includes(config)
        modules(
            firebaseModule,
            authModule,
            clipboardModule,
            sharedModule,
            platformModule
        )
    }
}
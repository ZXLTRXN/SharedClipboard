package com.example.sharedclipboard

import android.app.Application
import com.example.sharedclipboard.di.initKoin
import com.google.firebase.BuildConfig
import com.google.firebase.FirebaseApp
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Napier.base(DebugAntilog())

        initKoin {
            androidContext(this@MainApplication)
            androidLogger()
        }

        FirebaseApp.initializeApp(this)
    }
}
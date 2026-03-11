package com.example.sharedclipboard.di

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun setupNapier() {
    Napier.base(DebugAntilog())
}
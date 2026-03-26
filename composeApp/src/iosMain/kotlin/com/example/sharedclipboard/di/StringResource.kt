package com.example.sharedclipboard.di

import org.jetbrains.compose.resources.getString
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource

fun getStringFromRes(resource: StringResource): String {
    return runBlocking {
        getString(resource)
    }
}
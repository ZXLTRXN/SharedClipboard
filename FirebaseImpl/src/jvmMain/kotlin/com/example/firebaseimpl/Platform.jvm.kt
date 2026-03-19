package com.example.firebaseimpl

actual fun platform(): String {
    return "${System.getProperty("os.name")} ${System.getProperty("os.version")}"
}
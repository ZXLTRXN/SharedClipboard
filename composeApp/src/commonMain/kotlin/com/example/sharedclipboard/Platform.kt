package com.example.sharedclipboard

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
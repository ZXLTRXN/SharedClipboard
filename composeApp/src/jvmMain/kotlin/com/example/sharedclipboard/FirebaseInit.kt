package com.example.sharedclipboard

import android.app.Application
import com.google.firebase.FirebasePlatform
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize

fun initFirebase() {
    FirebasePlatform.initializeFirebasePlatform(object : FirebasePlatform() {
        val storage = mutableMapOf<String, String>()
        override fun store(key: String, value: String) = storage.set(key, value)
        override fun retrieve(key: String) = storage[key]
        override fun clear(key: String) { storage.remove(key) }
        override fun log(msg: String) = println(msg)

    })

    val options = FirebaseOptions(
        apiKey = "AIzaSyBHWvKwem5BnYU9WX3chRCFZngtJ_Kx13s",
        authDomain = "shared-clipboard-bc736.firebaseapp.com",
        databaseUrl = "https://shared-clipboard-bc736-default-rtdb.asia-southeast1.firebasedatabase.app",
        projectId = "shared-clipboard-bc736",
        storageBucket = "shared-clipboard-bc736.firebasestorage.app",
//        messagingSenderId = "620904450586",
        applicationId = "1:620904450586:web:b075ac49cca342d1615fa3",
//        measurementId = "G-3PKQ7DP995"
    )

    Firebase.initialize(Application(), options)
}
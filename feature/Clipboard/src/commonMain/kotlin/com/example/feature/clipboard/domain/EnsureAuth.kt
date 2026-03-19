package com.example.feature.clipboard.domain

import dev.gitlive.firebase.auth.FirebaseUser

interface EnsureAuth {

    suspend fun getUserOrNull(): FirebaseUser?
}
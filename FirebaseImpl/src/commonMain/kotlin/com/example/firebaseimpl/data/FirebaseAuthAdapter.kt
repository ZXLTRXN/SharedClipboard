package com.example.firebaseimpl.data

import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser
import io.mockative.Mockable

@Mockable
interface FirebaseAuthAdapter {
    val currentUser: FirebaseUser?
    suspend fun signInAnonymously(): AuthResult
}

class FirebaseAuthAdapterImpl(private val auth: FirebaseAuth) : FirebaseAuthAdapter {
    override val currentUser: FirebaseUser? = auth.currentUser
    override suspend fun signInAnonymously(): AuthResult = auth.signInAnonymously()
}
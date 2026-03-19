package com.example.sharedclipboard.data.models

import kotlinx.serialization.Serializable

@Serializable
data class InviteDto(
    val roomId: String,
    val expiresAt: Long
)
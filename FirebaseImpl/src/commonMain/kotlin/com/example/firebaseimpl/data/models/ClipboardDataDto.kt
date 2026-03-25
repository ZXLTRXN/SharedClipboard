package com.example.firebaseimpl.data.models

import kotlinx.serialization.Serializable

@Serializable // proguard fixme
data class ClipboardDataDto(
    val text: String = "",
    val timestamp: Long = 0L,
    val senderId: String = "",
    val senderName: String = ""
)
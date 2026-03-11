package com.example.sharedclipboard.data.models

import kotlinx.serialization.Serializable

@Serializable // proguard fixme
data class ClipboardDataDto(
    val text: String,
    val timestamp: Long,
    val senderId: String,
    val senderName: String
)
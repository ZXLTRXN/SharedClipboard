package com.example.feature.clipboard.ui.history

import com.example.core.ui.composables.formatTimestamp
import com.example.firebaseapi.domain.ClipModel

data class ClipUI(
    val timestamp: Long,
    val text: String,
    val senderName: String,
    val dateTime: String
)

fun ClipModel.toUI(): ClipUI = ClipUI(
    timestamp = timestamp,
    text = text,
    senderName = senderName,
    dateTime = timestamp.formatTimestamp()
)
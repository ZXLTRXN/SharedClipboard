package com.example.firebaseapi.domain

data class ClipModel(
    val timestamp: Long,
    val text: String,
    val senderName: String
) {
    companion object {
        val EMPTY = ClipModel(
            timestamp = 0L,
            text = "",
            senderName = ""
        )
    }
}
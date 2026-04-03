package com.example.firebaseapi.domain

data class ClipModel(
    val timestamp: Long,
    val text: String,
    val senderName: String
) {
    companion object {
        val LOADING = ClipModel(
            timestamp = -1L,
            text = "",
            senderName = ""
        )

        val TIMEOUT = ClipModel(
            timestamp = -2L,
            text = "",
            senderName = ""
        )
    }
}
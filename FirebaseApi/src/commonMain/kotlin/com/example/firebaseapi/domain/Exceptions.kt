package com.example.firebaseapi.domain

class ExpiredException(message: String = "Code is expired") : RuntimeException(message)
class CodeNotFoundException(message: String = "Code is not found", cause: Throwable? = null) : RuntimeException(message, cause)
class NoAttachedRoomException(message: String = "No room_id saved in storage") : RuntimeException(message)
package com.example.core.network

sealed class NetworkError(
    message: String?,
    cause: Throwable?
) : RuntimeException(
    message,
    cause
) {
    class NoInternet(
        cause: Throwable?
    ) : NetworkError(
        null,
        cause
    )

    class Serialization(
        cause: Throwable?
    ) : NetworkError(
        null,
        cause
    )

    class Timeout(
        cause: Throwable?
    ) : NetworkError(
        null,
        cause
    )

    open class Http(
        val code: Int,
        message: String?,
        cause: Throwable?
    ) : NetworkError(
        message,
        cause
    )

    class Unauthorized(
        message: String?,
        cause: Throwable?
    ) : Http(
        401,
        message,
        cause
    )

    class Unknown(
        message: String?,
        cause: Throwable?
    ) : NetworkError(
        message,
        cause
    )
}
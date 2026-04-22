package com.example.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

open class BaseClientFactory() {

    fun create(baseUrl: String): HttpClient {
        return HttpClient {
            expectSuccess = true

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(Logging) {
                level = LogLevel.ALL
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }

            install(UserAgent) {
                agent = "Ktor client"
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 5)
                exponentialDelay()
                modifyRequest { request ->
                    request.headers.append(
                        "x-retry-count",
                        retryCount.toString()
                    )
                }
            }
            install(HttpTimeout) // should be after HttpRequestRetry

            defaultRequest {
                url(baseUrl)

            }
//            install(Auth) {
//                bearer {
//                    loadTokens()
//                }
//            }
//            install(HttpCallValidator) { }
        }
    }
}

suspend inline fun <T> catchingNetworkErrors(request: suspend () -> T): Result<T> {
    return try {
        Result.success(request())
    } catch (e: Exception) {
        if (e is CancellationException) throw e

        val error = when (e) {
            is UnresolvedAddressException -> NetworkError.NoInternet(e)

            is SerializationException -> NetworkError.Serialization(e)

            is HttpRequestTimeoutException -> NetworkError.Timeout(e)

            is ResponseException -> {
                if (e.response.status == HttpStatusCode.Unauthorized) {
                    NetworkError.Unauthorized(
                        e.message,
                        e
                    )
                } else {
                    NetworkError.Http(
                        e.response.status.value,
                        e.message,
                        e
                    )
                }
            }

            else -> NetworkError.Unknown(
                e.message,
                e
            )
        }

        Result.failure(error)
    }
}
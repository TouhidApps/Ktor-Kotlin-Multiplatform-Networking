package com.touhidapps.ktor.network

import com.touhidapps.ktor.network.ResultNet

interface ErrorNet

sealed class NetworkError(val errMessage: String, val fullErrLog: String) : ErrorNet {

    data class RequestTimeout(
        val message: String = "REQUEST_TIMEOUT",
        val log: String = "Request timed out"
    ) : NetworkError(message, log)

    data class Unauthorized(
        val message: String = "UNAUTHORIZED",
        val log: String = "Unauthorized access"
    ) : NetworkError(message, log)

    data class Conflict(
        val message: String = "CONFLICT",
        val log: String = "Conflict occurred"
    ) : NetworkError(message, log)

    data class TooManyRequests(
        val message: String = "TOO_MANY_REQUESTS",
        val log: String = "Too many requests"
    ) : NetworkError(message, log)

    data class NoInternet(
        val message: String = "NO INTERNET",
        val log: String = "No internet connection"
    ) : NetworkError(message, log)

    data class PayloadTooLarge(
        val message: String = "PAYLOAD_TOO_LARGE",
        val log: String = "Payload is too large"
    ) : NetworkError(message, log)

    data class ServerError(
        val message: String = "SERVER_ERROR",
        val log: String = "Server error"
    ) : NetworkError(message, log)

    data class Serialization(
        val message: String = "SERIALIZATION",
        val log: String = "Serialization error"
    ) : NetworkError(message, log)

    data class Unknown(
        val message: String = "UNKNOWN",
        val log: String = "UNKNOWN error"
    ) : NetworkError(message, log)


    companion object {
        fun getErrorByCode(code: Int): NetworkError {
            return when (code) {
                1 -> RequestTimeout()  // Default message for RequestTimeout
                2 -> PayloadTooLarge() // Default message for PayloadTooLarge
                else -> Unknown()
            }
        }
    }
}



sealed interface ResultNet<out D, out E: ErrorNet> {
    data class Success<out D>(val data: D): ResultNet<D, Nothing>
    data class Error<out E: ErrorNet>(val error: E): ResultNet<Nothing, E>
//    class Loading : ResultNet<Nothing, Nothing>
}

inline fun <T, E: ErrorNet, R> ResultNet<T, E>.map(map: (T) -> R): ResultNet<R, E> {
    return when(this) {
        is ResultNet.Error -> ResultNet.Error(error)
        is ResultNet.Success -> ResultNet.Success(map(data))
//        is ResultNet.Loading -> ResultNet.Loading
    }
}

fun <T, E: ErrorNet> ResultNet<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map {  }
}

inline fun <T, E: ErrorNet> ResultNet<T, E>.onSuccess(action: (T) -> Unit): ResultNet<T, E> {
    return when(this) {
        is ResultNet.Error -> this
        is ResultNet.Success -> {
            action(data)
            this
        }
//        is ResultNet.Loading -> this
    }
}

inline fun <T, E: ErrorNet> ResultNet<T, E>.onError(action: (E) -> Unit): ResultNet<T, E> {
    return when(this) {
        is ResultNet.Error -> {
            action(error)
            this
        }
        is ResultNet.Success -> this
    }
}

typealias EmptyResult<E> = ResultNet<Unit, E>
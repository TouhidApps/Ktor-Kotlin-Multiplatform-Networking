package com.touhidapps.ktor.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.json.Json


fun createHttpClient() = HttpClient {

    defaultRequest { // Default items for all api call
        url {
            takeFrom(AllApi.BASE_URL)
          //  parameters.append("api_key", BuildConfig.API_KEY)
//            headers.appendIfNameAbsent(HttpHeaders.ContentType,
//                ContentType.Application.Json.contentType)
        }
    }

    install(HttpTimeout) {
        val timeout = 30000L
        connectTimeoutMillis = timeout
        requestTimeoutMillis = timeout
        socketTimeoutMillis = timeout
    }

    install(ContentNegotiation) {
        json(
            json = Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    headers {
        append(HttpHeaders.Accept, "application/json")
        append(HttpHeaders.Authorization, "my_token")
        append(HttpHeaders.UserAgent, "ktor_client")
    }

//        install(Auth) {
//            bearer {
//                loadTokens {  }
//                refreshTokens {  }
//            }
//        }

    expectSuccess = false // Allow failed responses to be handled manually
    HttpResponseValidator {

        var myResp: HttpResponse? = null
        // Custom response & response code handle
        validateResponse { response ->

            myResp = response

            if (response.status.value > 299) {
                throw Exception(getUserFriendlyError(response))
            }


//            when(statusCode) {
//                in 300..399 -> throw RedirectResponseException(response, cachedResponseText = "n/a")
//                in 400..499 -> throw ClientRequestException(response, cachedResponseText = "n/a")
//                in 500..599 -> throw ServerResponseException(response, cachedResponseText = "n/a")
//            }
//
//            if (statusCode >= 600) {
//                throw ResponseException(response, cachedResponseText = "n/a")
//            }

        }

        handleResponseException { cause, request ->

            if (cause !is UnresolvedAddressException) { // UnresolvedAddressException is no internet
                sendErrorLogToServer(cause, myResp)
            }

//            if (cause is ClientRequestException || cause is ServerResponseException) {
//                // Send error log to Firebase or server
//                sendErrorLogToServer(cause)
//            } else {
//                // Optionally handle unexpected exceptions
//                println("Unexpected error: ${cause.localizedMessage}")
//            }
        }

    }


    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                println("---------------------------START")
                println(message)
                println("---------------------------END")
            }
        }
        level = LogLevel.ALL
//        filter { request ->
//            request.url.host.contains("jsonplaceholder")
//        }
        // Don't show sensitive data like auth data in log
        sanitizeHeader { header ->
            header == HttpHeaders.Authorization
        }
    }

}

/**
 * This is to show users
 */
fun getUserFriendlyError(response: HttpResponse): String {
    val statusCode = response.status.value

    var msg = when(statusCode) {

        // 200 range is success
//                200 -> {
//                    "Success Request"
//                }
//                201 -> {
//                    "Created"
//                }
//                202 -> {
//                    "Accepted"
//                }
//                204 -> {
//                    "No Content"
//                }
        400 -> {
            "Bad Request"
        }
        401 -> {
            "Unauthorized"
        }
        402 -> {
            "Payment Required"
        }
        403 -> {
            "Forbidden"
        }
        404 -> {
            "Not Found"
        }
        405 -> {
            "Method Not Allowed"
        }
        406 -> {
            "Not Acceptable"
        }
        407 -> {
            "Proxy Authentication Required"
        }
        408 -> {
            "Request Timeout"
        }
        409 -> {
            "Conflict"
        }
        410 -> {
            "Gone"
        }
        411 -> {
            "Length Required"
        }
        412 -> {
            "Precondition Failed"
        }
        413 -> {
            "Payload Too Large"
        }
        414 -> {
            "URI Too Long"
        }
        415 -> {
            "Unsupported Media Type"
        }
        416 -> {
            "Range Not Satisfiable"
        }
        417 -> {
            "Expectation Failed"
        }
        429 -> {
            "Too Many Requests"
        }
        500 -> {
            "Internal Server Error"
        }
        501 -> {
            "Not Implemented"
        }
        502 -> {
            "Bad Gateway"
        }
        503 -> {
            "Service Unavailable"
        }
        504 -> {
            "Gateway Timeout"
        }
        505 -> {
            "HTTP Version Not Supported"
        }
        else -> {
            "Unknown Error"
        }

    }

    msg += " [${statusCode}]"
    return msg
}

/**
 * Error log should send to firebase or http api call
 * without using ktor as this is to keep ktors exception
 */
fun sendErrorLogToServer(cause: Throwable, resp: HttpResponse?) {
    // Example: Send the stack trace or message to server/Firebase
  //  println("Sending error log to server: ${cause.localizedMessage}")
    // Here you would send it to a Firebase database, server, or logging service
}




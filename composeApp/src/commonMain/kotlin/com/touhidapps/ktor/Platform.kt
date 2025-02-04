package com.touhidapps.ktor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
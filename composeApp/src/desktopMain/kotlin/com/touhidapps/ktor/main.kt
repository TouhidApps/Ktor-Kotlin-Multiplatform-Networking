package com.touhidapps.ktor

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Ktor Kotlin Multiplatform Networking",
    ) {
        App()
    }
}
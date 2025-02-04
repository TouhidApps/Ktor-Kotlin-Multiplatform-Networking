package com.touhidapps.ktor.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel // lifecycle-viewmodel
import com.touhidapps.ktor.repository.FlowerRepository
import org.koin.compose.currentKoinScope
import org.koin.core.parameter.parametersOf


@Composable
inline fun <reified T : ViewModel> koinViewModel(vararg parameters: Any): T {
    val scope = currentKoinScope()
    return viewModel<T> {
        scope.get<T> {
            parametersOf(*parameters)
        }
    }
}



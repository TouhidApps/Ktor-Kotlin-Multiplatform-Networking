package com.touhidapps.ktor.di

import com.touhidapps.ktor.network.createHttpClient
import com.touhidapps.ktor.repository.FlowerRepository
import com.touhidapps.ktor.viewModel.FlowerViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

//val appModule = module {
//   // singleOf(::FlowerViewModel)
//    factoryOf(::FlowerViewModel)
//   // factory { MyViewModel(get()) }
//}

val appModule = module {
    single { FlowerRepository(createHttpClient()) } // Define how FlowerRepository is provided

    viewModel { FlowerViewModel(get()) } // Use `get()` to inject FlowerRepository into FlowerViewModel
}
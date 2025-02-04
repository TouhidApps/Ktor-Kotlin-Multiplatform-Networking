package com.touhidapps.ktor.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel // lifecycle-viewmodel
import com.touhidapps.ktor.repository.FlowerRepository
import com.touhidapps.ktor.viewModel.FlowerViewModel
import org.koin.compose.currentKoinScope
import org.koin.dsl.module


//val myModule = module {
//    // A generic factory definition that can handle parameters dynamically
//    viewModel { (parameters: Array<out Any>) ->
//        // Example: MyViewModel requires a single parameter (String)
//        FlowerViewModel(parameters[0] as String)
//    }
//}



val myModule = module {
    // A generic ViewModel factory to handle different ViewModels with parameters

//    viewModel { (parameters: Array<out Any>) ->
//        when {
//            parameters.size == 1 -> FlowerViewModel(parameters[0] as FlowerRepository)
////            parameters.size == 2 -> AnotherViewModel(parameters[0] as Int, parameters[1] as Boolean)
////            parameters.size == 2 -> FlowerViewModel(
////                parameters[0] as String, // flowerName
////                parameters[1] as Int     // numberOfPetals
////            )
//            else -> throw IllegalArgumentException("Unsupported parameter count")
//        }
//    }
}


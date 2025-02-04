package com.touhidapps.ktor.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.touhidapps.ktor.model.FlowersModel
import com.touhidapps.ktor.network.NetworkError
import com.touhidapps.ktor.network.ResultNet
import com.touhidapps.ktor.network.onError
import com.touhidapps.ktor.network.onSuccess
import com.touhidapps.ktor.repository.FlowerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class FlowerViewModel(private val repository: FlowerRepository): ViewModel() {

    private var _flowers: MutableStateFlow<FlowersModel> = MutableStateFlow(FlowersModel())
    val flowers: StateFlow<FlowersModel> = _flowers.asStateFlow()

    private val _errorMessage = MutableStateFlow<String>("")
    val errorMessage get() = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading get() = _isLoading.asStateFlow()

    fun getFlowersData() {

        _isLoading.value = true
        _errorMessage.value = ""

        viewModelScope.launch {

            repository.getFlowers()
                .onSuccess { model ->
                    // Modify data here if necessary
                    _flowers.value = model
                }
                .onError { error ->
                    _errorMessage.value = error.errMessage
                }

            _isLoading.value = false

        }

    } // getFlowersData



}
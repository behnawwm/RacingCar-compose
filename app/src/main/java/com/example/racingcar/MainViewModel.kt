package com.example.racingcar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _accelerationX = MutableStateFlow(0f)
    val accelerationX = _accelerationX.asStateFlow()

    fun setAccelerationX(accelerationX: Float) {
        _accelerationX.value = accelerationX
    }
}
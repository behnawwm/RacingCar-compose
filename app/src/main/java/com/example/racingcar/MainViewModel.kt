package com.example.racingcar

import androidx.lifecycle.ViewModel
import com.example.racingcar.Constants.DEFAULT_ACCELEROMETER_SENSITIVITY
import com.example.racingcar.models.AccelerationData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _acceleration = MutableStateFlow(AccelerationData(0f, 0f, 0f))
    val acceleration = _acceleration.asStateFlow()

    fun setAcceleration(
        accelerationX: Float,
        accelerationY: Float,
        accelerationZ: Float,
        sensitivity: Int = DEFAULT_ACCELEROMETER_SENSITIVITY
    ) {
        _acceleration.update {
            it.copy(
                x = accelerationX * sensitivity,
                y = accelerationY * sensitivity,
                z = accelerationZ * sensitivity
            )
        }
    }


}
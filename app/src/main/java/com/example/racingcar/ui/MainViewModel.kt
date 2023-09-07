package com.example.racingcar.ui

import androidx.lifecycle.ViewModel
import com.example.racingcar.models.AccelerationData
import com.example.racingcar.models.MovementInput
import com.example.racingcar.utils.Constants.DEFAULT_ACCELEROMETER_SENSITIVITY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private val _acceleration = MutableStateFlow(AccelerationData(0f, 0f, 0f))
    val acceleration = _acceleration.asStateFlow()

    private val _movementInput = MutableStateFlow(MovementInput.Swipe)
    val movementInput = _movementInput.asStateFlow()

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

    fun setMovementInput(movementInput: MovementInput) {
        _movementInput.update {
            movementInput
        }

    }


}
package com.example.racingcar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.racingcar.Constants.DEFAULT_ACCELEROMETER_SENSITIVITY
import com.example.racingcar.Constants.INITIAL_GAME_SCORE
import com.example.racingcar.models.AccelerationData
import com.example.racingcar.models.MovementInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _acceleration = MutableStateFlow(AccelerationData(0f, 0f, 0f))
    val acceleration = _acceleration.asStateFlow()

    private val _movementInput = MutableStateFlow(MovementInput.Accelerometer)
    val movementInput = _movementInput.asStateFlow()

    private val _gameScore = MutableStateFlow(Constants.INITIAL_GAME_SCORE)
    val gameScore = _gameScore.asStateFlow()

    val vibrateSharedFlow = MutableStateFlow<Long>(-1)

    private val collisionStateFlow = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            collisionStateFlow.collect { hasCollision ->
                if (hasCollision) {
                    _gameScore.update { currentScore ->
                        val newScore = currentScore - Constants.COLLISION_SCORE_PENALTY
                        newScore.takeIf { it > INITIAL_GAME_SCORE } ?: INITIAL_GAME_SCORE
                    }
                    vibrateSharedFlow.tryEmit(System.currentTimeMillis())
                }
            }
        }
    }

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
        _movementInput.update { movementInput }
    }


    fun updateCollision(hasCollision: Boolean) {
        collisionStateFlow.update { hasCollision }
    }

    fun increaseGameScore() {
        _gameScore.update { currentScore ->
            currentScore + 1
        }
    }

    fun resetGameScore() {
        _gameScore.update { INITIAL_GAME_SCORE }
    }


}
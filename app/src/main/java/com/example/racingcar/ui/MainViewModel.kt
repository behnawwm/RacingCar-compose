package com.example.racingcar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.racingcar.domain.usecase.GetHighscoreUseCase
import com.example.racingcar.domain.usecase.SaveHighscoreUseCase
import com.example.racingcar.models.AccelerationData
import com.example.racingcar.models.MovementInput
import com.example.racingcar.utils.Constants.COLLISION_SCORE_PENALTY
import com.example.racingcar.utils.Constants.DEFAULT_ACCELEROMETER_SENSITIVITY
import com.example.racingcar.utils.Constants.INITIAL_GAME_SCORE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getHighscoreUseCase: GetHighscoreUseCase,
    private val saveHighscoreUseCase: SaveHighscoreUseCase,
) : ViewModel() {

    private val _acceleration = MutableStateFlow(AccelerationData(0f, 0f, 0f))
    val acceleration = _acceleration.asStateFlow()

    private val _movementInput = MutableStateFlow(MovementInput.Gestures)
    val movementInput = _movementInput.asStateFlow()

    private val _gameScore = MutableStateFlow(INITIAL_GAME_SCORE)
    val gameScore = _gameScore.asStateFlow()

    private val _highscore = MutableStateFlow(0)
    val highscore = _highscore.asStateFlow()

    val vibrateSharedFlow = MutableSharedFlow<Unit>(replay = 1)

    private val collisionStateFlow = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            collisionStateFlow.collect { hasCollision ->
                if (hasCollision) {
                    _gameScore.update { currentScore ->
                        val newScore = currentScore - COLLISION_SCORE_PENALTY
                        newScore.takeIf { it > INITIAL_GAME_SCORE } ?: INITIAL_GAME_SCORE
                    }
                    vibrateSharedFlow.tryEmit(Unit)
                }
            }
        }
        viewModelScope.launch {
            getHighscoreUseCase.execute().collect {
                _highscore.value = it
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
            (currentScore + 1).also { newScore ->
                saveNewHighscore(newScore)
            }
        }
    }

    private fun saveNewHighscore(newScore: Int) {
        viewModelScope.launch {
            saveHighscoreUseCase.execute(newScore)
        }
    }

    fun resetGameScore() {
        _gameScore.update { INITIAL_GAME_SCORE }
    }


}
package com.example.racingcar.domain.usecase

import com.example.racingcar.domain.repo.HighscoreRepository
import kotlinx.coroutines.flow.Flow

class GetHighscoreUseCase(
    private val highscoreRepository: HighscoreRepository,
) {
    fun execute(): Flow<Int> = highscoreRepository.getHighScore()
}
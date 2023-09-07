package com.example.racingcar.domain.usecase

import com.example.racingcar.domain.repo.HighscoreRepository
import kotlinx.coroutines.flow.first

class SaveHighscoreUseCase(
    private val highscoreRepository: HighscoreRepository,
) {

    suspend fun execute(score: Int) {
        val currentHighscore = highscoreRepository.getHighScore().first()
        if (score > currentHighscore) {
            highscoreRepository.saveHighScore(score)
        }
    }
}
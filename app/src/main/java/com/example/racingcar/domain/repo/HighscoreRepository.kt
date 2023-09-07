package com.example.racingcar.domain.repo

import kotlinx.coroutines.flow.Flow

interface HighscoreRepository {

    suspend fun saveHighScore(score: Int)

    suspend fun getHighScore(): Flow<Int>
}
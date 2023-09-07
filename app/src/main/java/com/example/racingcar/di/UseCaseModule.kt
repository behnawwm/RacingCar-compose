package com.example.racingcar.di

import com.example.racingcar.domain.repo.HighscoreRepository
import com.example.racingcar.domain.usecase.GetHighscoreUseCase
import com.example.racingcar.domain.usecase.SaveHighscoreUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providesGetHighscoreUseCase(
        highscoreRepository: HighscoreRepository
    ): GetHighscoreUseCase {
        return GetHighscoreUseCase(highscoreRepository)
    }

    @Provides
    @Singleton
    fun providesSaveHighscoreUseCase(
        highscoreRepository: HighscoreRepository
    ): SaveHighscoreUseCase {
        return SaveHighscoreUseCase(highscoreRepository)
    }
}
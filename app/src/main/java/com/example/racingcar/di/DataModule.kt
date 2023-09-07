package com.example.racingcar.di

import android.content.Context
import com.example.racingcar.data.repo.HighscoreRepositoryImpl
import com.example.racingcar.data.source.highscoreDataStore
import com.example.racingcar.domain.repo.HighscoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providesHighscoreRepository(
        @ApplicationContext context: Context
    ): HighscoreRepository {
        return HighscoreRepositoryImpl(context.highscoreDataStore)
    }


}
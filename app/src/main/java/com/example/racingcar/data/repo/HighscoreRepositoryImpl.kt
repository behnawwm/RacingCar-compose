package com.example.racingcar.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.racingcar.domain.repo.HighscoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class HighscoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : HighscoreRepository {

    override suspend fun saveHighScore(score: Int) {
        dataStore.edit {
            it[HIGHSCORE_DATASTORE_KEY] = score
        }
    }

    override fun getHighScore(): Flow<Int> {
        return dataStore.data.map {
            it[HIGHSCORE_DATASTORE_KEY] ?: 0
        }
    }

    companion object {
        val HIGHSCORE_DATASTORE_KEY = intPreferencesKey("highscore_datastore_key")
    }

}
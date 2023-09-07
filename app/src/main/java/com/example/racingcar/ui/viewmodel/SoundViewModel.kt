package com.example.racingcar.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.racingcar.R
import com.example.racingcar.utils.SoundRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SoundViewModel @Inject constructor(
    private val soundRepository: SoundRepository
) : ViewModel() {

    init {
        soundRepository.loadSound(NEW_HIGHSCORE_SOUND_ID, R.raw.new_highscore)
        soundRepository.loadSound(BLOCKER_HIT_SOUND_ID, R.raw.blocker_hit)
        soundRepository.loadSound(MILESTONE_REACH_SOUND_ID, R.raw.milestone_reach)
    }

    fun playNewHighscoreSound() {
        soundRepository.playSound(NEW_HIGHSCORE_SOUND_ID)
    }

    fun playBlockerHitSound() {
        soundRepository.playSound(BLOCKER_HIT_SOUND_ID)
    }

    fun playMilestoneReachSound() {
        soundRepository.playSound(MILESTONE_REACH_SOUND_ID)
    }


    fun playBackgroundMusic() {
        soundRepository.playBackgroundMusic()
    }

    fun stopBackgroundMusic() {
        soundRepository.stopBackgroundMusic()
    }

    fun release() {
        soundRepository.release()
    }

    companion object {
        const val NEW_HIGHSCORE_SOUND_ID = 1
        const val BLOCKER_HIT_SOUND_ID = 2
        const val MILESTONE_REACH_SOUND_ID = 3
    }

}
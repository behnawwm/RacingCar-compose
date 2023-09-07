package com.example.racingcar.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import com.example.racingcar.R

class SoundManager(private val context: Context) {
    private val soundPool: SoundPool
    private val soundMap: HashMap<Int, Int> = HashMap()
    private var mediaPlayer: MediaPlayer? = null

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_GAME)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    fun loadSound(soundId: Int, soundResourceId: Int) {
        val sound = soundPool.load(context, soundResourceId, 1)
        soundMap[soundId] = sound
    }

    fun playSound(soundId: Int) {
        soundMap[soundId]?.let {
            soundPool.play(it, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    fun playBackgroundMusic(musicResourceId: Int = R.raw.background) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, musicResourceId)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    fun stopBackgroundMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun release() {
        soundPool.release()
        mediaPlayer?.release()
    }
}
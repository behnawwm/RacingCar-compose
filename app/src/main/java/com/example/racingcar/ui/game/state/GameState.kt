package com.example.racingcar.ui.game.state

data class GameState(val status: Status = Status.STOPPED) {
    enum class Status {
        RUNNING,
        PAUSED,
        STOPPED
    }

    fun isRunning() = status == Status.RUNNING
    fun isPaused() = status == Status.PAUSED
    fun isStopped() = status == Status.STOPPED
}

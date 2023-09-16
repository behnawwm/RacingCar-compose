package com.example.racingcar.ui.game.state

data class GameState(private var status: Status = Status.STOPPED) {
    enum class Status {
        RUNNING,
        PAUSED,
        STOPPED
    }

    fun isRunning() = status == Status.RUNNING
    fun isPaused() = status == Status.PAUSED
    fun isStopped() = status == Status.STOPPED

    fun run() {
        status = Status.RUNNING
    }

    fun pause() {
        status = Status.PAUSED
    }

    fun stop() {
        status = Status.STOPPED
    }

    fun getStatusName(): String {
        return status.name
    }
}

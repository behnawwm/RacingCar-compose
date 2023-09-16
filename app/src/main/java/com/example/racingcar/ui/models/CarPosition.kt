package com.example.racingcar.ui.models

enum class CarPosition {
    Right {
        override fun fromLeftOffsetIndex(): Float = 2f
    },
    Middle {
        override fun fromLeftOffsetIndex(): Float = 1f
    },
    Left {
        override fun fromLeftOffsetIndex(): Float = 0f
    };

    abstract fun fromLeftOffsetIndex(): Float
}
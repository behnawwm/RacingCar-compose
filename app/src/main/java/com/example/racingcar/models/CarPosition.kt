package com.example.racingcar.models

enum class CarPosition {
    Right {
        override fun fromLeftOffsetIndex(): Int = 2
    },
    Middle {
        override fun fromLeftOffsetIndex(): Int = 1
    },
    Left {
        override fun fromLeftOffsetIndex(): Int = 0
    };

    abstract fun fromLeftOffsetIndex(): Int
}
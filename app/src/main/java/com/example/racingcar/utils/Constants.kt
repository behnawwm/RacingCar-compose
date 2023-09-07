package com.example.racingcar.utils

object Constants {

    // game properties
    const val LANE_COUNT = 3
    const val INITIAL_VELOCITY = 5
    const val GAME_SCORE_TO_VELOCITY_RATIO = 2
    const val INITIAL_GAME_SCORE = 0
    const val SWIPE_MIN_OFFSET_FROM_MAX_WIDTH = 6 //todo rename
    const val ACCELERATION_X_Y_OFFSET_TRIGGER = 15
    const val DEFAULT_ACCELEROMETER_SENSITIVITY = 1
    const val COLLISION_SCORE_PENALTY = 5

    // positioning
    const val CAR_POSITION_PERCENTAGE_FROM_BOTTOM = 20
    const val BLOCKER_INTERSPACE_PERCENTAGE = 25
    const val STREET_SIDE_PERCENTAGE_EACH = 9  // approximate percentage based on current road!

    // asset size
    const val BLOCKER_HEIGHT = 63
    const val BLOCKER_WIDTH = 216
    const val CAR_SIZE = 208

    // misc
    const val TICKER_ANIMATION_DURATION = 250
    const val CAR_MOVEMENT_SPRING_ANIMATION_STIFFNESS = 2500f
}
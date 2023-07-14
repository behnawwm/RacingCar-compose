package com.example.racingcar.state

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.racingcar.models.CarPosition
import com.example.racingcar.models.CarPosition.*
import com.example.racingcar.Constants.LANE_COUNT
import com.example.racingcar.Constants.STREET_SIDE_PERCENTAGE_EACH
import com.example.racingcar.models.SwipeDirection

data class CarState(val image: ImageBitmap, val initialState: CarPosition = Middle) {
    private var currentPosX: Int = Middle.fromLeftOffsetIndex()
    var position: CarPosition = Middle
        private set

    fun move(direction: SwipeDirection) {
        when (direction) {
            SwipeDirection.Right -> moveRight()
            SwipeDirection.Left -> moveLeft()
        }
    }

    private fun moveRight() {
        position = when (position) {
            Right -> Right
            Middle -> Right
            Left -> Middle
        }
    }

    private fun moveLeft() {
        position = when (position) {
            Right -> Middle
            Middle -> Left
            Left -> Left
        }
    }


    fun draw(drawScope: DrawScope) { //todo calculate `size` once per init
        drawScope.apply {
            //todo change based on size.width
            val carSize = 208 //size.width.toInt() * STREET_SIDE_PERCENTAGE_EACH / 100
            val initialOffsetX = (size.width.toInt() * STREET_SIDE_PERCENTAGE_EACH / 100)
            val laneSize =
                (size.width.toInt() * (100 - (2 * STREET_SIDE_PERCENTAGE_EACH)) / 100) / LANE_COUNT
            val carOffsetX =
                initialOffsetX +
                        (laneSize / 2) - (carSize / 2) +
                        (laneSize * position.fromLeftOffsetIndex())

            drawImage(
                //todo make car bigger
                image = image,
                dstOffset = IntOffset(
                    x = carOffsetX,
                    y = size.height.toInt() - 500 //todo change to percent
                ),
                srcSize = IntSize(carSize, carSize)
            )
        }
    }

}
package com.example.racingcar

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.racingcar.CarPosition.*

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

            val blockerOffsetX = (size.width.toInt() * STREET_SIDE_PERCENTAGE_EACH / 100)
            val laneSize =
                (size.width.toInt() * (100 - (2 * STREET_SIDE_PERCENTAGE_EACH)) / 100) / LANE_COUNT
            val carOffsetX =
                blockerOffsetX +
                        (laneSize / 2) - (carSize / 2) +
                        (laneSize * position.fromLeftOffsetIndex())

//        drawRect(
//            color = Color.Red,
//            size = Size(carSize.toFloat(), carSize.toFloat()),
//            topLeft = Offset(
//                x = carOffsetX.toFloat(),
//                y = (size.height.toInt() - 500).toFloat() //todo change to percent
//            )
//        )
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

    companion object {
        const val STREET_SIDE_PERCENTAGE_EACH = 9  // approximate percentage
        const val LANE_COUNT = 3
    }
}
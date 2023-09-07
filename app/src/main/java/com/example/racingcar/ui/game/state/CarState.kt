package com.example.racingcar.ui.game.state

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.unit.toSize
import com.example.racingcar.models.AccelerationData
import com.example.racingcar.models.CarPosition
import com.example.racingcar.models.CarPosition.Left
import com.example.racingcar.models.CarPosition.Middle
import com.example.racingcar.models.CarPosition.Right
import com.example.racingcar.models.SwipeDirection
import com.example.racingcar.utils.Constants.ACCELERATION_X_Y_OFFSET_TRIGGER
import com.example.racingcar.utils.Constants.CAR_POSITION_PERCENTAGE_FROM_BOTTOM
import com.example.racingcar.utils.Constants.CAR_SIZE
import com.example.racingcar.utils.Constants.LANE_COUNT
import com.example.racingcar.utils.Constants.STREET_SIDE_PERCENTAGE_EACH

data class CarState(
    val image: ImageBitmap,
    var position: CarPosition = Middle
) {

    fun draw(
        drawScope: DrawScope,
        positionFromLeftOffset: Float
    ): Rect { //todo calculate `size` once per init
        drawScope.apply {
            val initialOffsetX = (size.width.toInt() * STREET_SIDE_PERCENTAGE_EACH / 100)
            val laneSize =
                (size.width.toInt() * (100 - (2 * STREET_SIDE_PERCENTAGE_EACH)) / 100) / LANE_COUNT
            val carOffsetX =
                initialOffsetX +
                        (laneSize / 2) - (CAR_SIZE / 2) +
                        (laneSize * positionFromLeftOffset).toInt()

            val dstOffset = IntOffset(
                x = carOffsetX,
                y = size.height.toInt() - (size.height.toInt() * CAR_POSITION_PERCENTAGE_FROM_BOTTOM / 100)
            )
            val dstSize = IntSize(width = CAR_SIZE, height = CAR_SIZE)

            drawImage(
                image = image,
                dstOffset = dstOffset,
                dstSize = dstSize
            )
            return Rect(offset = dstOffset.toOffset(), size = dstSize.toSize())
        }
    }

    fun moveWithGesture(swipeDirection: SwipeDirection) {
        when (swipeDirection) {
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

    fun moveWithAcceleration(acceleration: AccelerationData) {
        //todo not working when phone is on a surface (x=0?)
        val ratio = acceleration.x * acceleration.y
        position = if (ratio > ACCELERATION_X_Y_OFFSET_TRIGGER)
            Left
        else if (ratio < -ACCELERATION_X_Y_OFFSET_TRIGGER)
            Right
        else
            Middle
    }

}
package com.example.racingcar.ui.game.state

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.unit.toSize
import com.example.racingcar.Constants.BLOCKER_HEIGHT
import com.example.racingcar.Constants.BLOCKER_INTERSPACE_PERCENTAGE
import com.example.racingcar.Constants.BLOCKER_WIDTH
import com.example.racingcar.Constants.LANE_COUNT
import com.example.racingcar.Constants.STREET_SIDE_PERCENTAGE_EACH
import kotlin.random.Random

data class BlockState(val image: ImageBitmap, var lanePosition: Int) {
    private var currentPosY = 0

    fun move(velocity: Int) {
        currentPosY += velocity
    }

    fun draw(drawScope: DrawScope, index: Int, onDraw: (Rect) -> Unit) {
        drawScope.apply {
            //todo change based on size.width
            val initialOffsetX = (size.width.toInt() * STREET_SIDE_PERCENTAGE_EACH / 100)
            val laneSize =
                (size.width.toInt() * (100 - (2 * STREET_SIDE_PERCENTAGE_EACH)) / 100) / LANE_COUNT

            val blockerOffsetX =
                initialOffsetX +
                        ((laneSize - BLOCKER_WIDTH) / 2) +
                        (laneSize * lanePosition)

            val indexOffset = (index * (size.height * BLOCKER_INTERSPACE_PERCENTAGE / 100).toInt())
            val blockerOffsetY = currentPosY - indexOffset


            if (currentPosY > size.height + indexOffset) {
                currentPosY = indexOffset
                //todo change position to avoid putting 2 consecutive blockers in same position
                lanePosition = Random.nextInt(from = 0, until = LANE_COUNT)
            }

            if (blockerOffsetY < size.height) {
                val dstOffset = IntOffset(x = blockerOffsetX, y = blockerOffsetY)
                val dstSize = IntSize(width = BLOCKER_WIDTH, height = BLOCKER_HEIGHT)

                drawImage(
                    image = image,
                    dstOffset = dstOffset,
                    dstSize = dstSize
                )
                onDraw(
                    Rect(
                        offset = dstOffset.toOffset(),
                        size = dstSize.toSize()
                    )
                )
            }
        }
    }
}
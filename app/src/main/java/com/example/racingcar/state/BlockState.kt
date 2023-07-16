package com.example.racingcar.state

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.racingcar.Constants.BLOCKER_HEIGHT
import com.example.racingcar.Constants.BLOCKER_INTERSPACE_PERCENTAGE
import com.example.racingcar.Constants.BLOCKER_WIDTH
import com.example.racingcar.Constants.LANE_COUNT
import com.example.racingcar.Constants.STREET_SIDE_PERCENTAGE_EACH
import com.example.racingcar.models.Blocker

data class BlockState(val image: ImageBitmap) {
    private val repetitionCount = 5 //todo magic numbers
    private val blockers = List(repetitionCount) {//todo fix multiple blocker in same position
        generateRandomLanesSegment()
    }.flatten().toMutableList()

    private fun generateRandomLanesSegment() =
        (0 until LANE_COUNT).shuffled().map {
            Blocker(id = System.currentTimeMillis(), lanePosition = it)
        }

    private var currentPosY = 0

    fun move(velocity: Int) {
        currentPosY += velocity
    }

    fun draw(drawScope: DrawScope) {
        drawScope.apply {
            //todo change based on size.width
            val initialOffsetX = (size.width.toInt() * STREET_SIDE_PERCENTAGE_EACH / 100)
            val laneSize =
                (size.width.toInt() * (100 - (2 * STREET_SIDE_PERCENTAGE_EACH)) / 100) / LANE_COUNT

            blockers.forEachIndexed { index, blocker ->
                val blockerOffsetX =
                    initialOffsetX +
                            ((laneSize - BLOCKER_WIDTH) / 2) +
                            (laneSize * blocker.lanePosition)

                val blockerOffsetY =
                    currentPosY - (index * (size.height * BLOCKER_INTERSPACE_PERCENTAGE / 100).toInt())

                if (blockerOffsetY < size.height)
                    drawImage(
                        image = image,
                        srcOffset = IntOffset(0, 0),
                        dstOffset = IntOffset(
                            x = blockerOffsetX,
                            y = blockerOffsetY
                        ),
                        dstSize = IntSize(width = BLOCKER_WIDTH, height = BLOCKER_HEIGHT)
                    )
            }
        }
    }
}
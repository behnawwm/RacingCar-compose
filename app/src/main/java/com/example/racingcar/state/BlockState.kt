package com.example.racingcar.state

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.racingcar.Constants.BLOCKER_HEIGHT
import com.example.racingcar.Constants.BLOCKER_WIDTH
import com.example.racingcar.models.Blocker
import com.example.racingcar.Constants.LANE_COUNT
import com.example.racingcar.Constants.STREET_SIDE_PERCENTAGE_EACH

data class BlockState(val image: ImageBitmap) {
    private var blockers = mutableListOf<Blocker>(
        Blocker(0, 0),
        Blocker(1, 1),
        Blocker(2, 2),
    )

    private var currentPosY = 0

    fun move(velocity: Int) {
        currentPosY += velocity
    }

    fun draw(drawScope: DrawScope) {
        Log.d("mamad", "blocker y: $currentPosY")
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
                drawImage(
                    image = image,
                    srcOffset = IntOffset(0, 0),
                    dstOffset = IntOffset(
                        x = blockerOffsetX,
                        y = currentPosY - (index * 400)
                    ),
                    dstSize = IntSize(width = BLOCKER_WIDTH, height = BLOCKER_HEIGHT)
                )
            }
        }
    }
}
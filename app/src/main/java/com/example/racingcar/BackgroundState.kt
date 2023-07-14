package com.example.racingcar

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

data class BackgroundState(val image: ImageBitmap) {
    private var currentPosY = 0

    fun move(velocity: Int) {
        currentPosY += velocity
    }

    fun draw(drawScope: DrawScope, initialPos: Int = 0) {
        drawScope.draw()
    }

    private fun DrawScope.draw() {
        if (currentPosY > 2 * size.width) { //todo fix lag
            currentPosY = 0
        }
        drawImage(
            image = image,
            srcOffset = IntOffset(0, 0),
            dstOffset = IntOffset(0, currentPosY),
            dstSize = IntSize(size.width.toInt(), size.height.toInt())
        )
        drawImage(
            image = image,
            srcOffset = IntOffset(0, 0),
            dstOffset = IntOffset(0, -size.height.toInt() + currentPosY),
            dstSize = IntSize(size.width.toInt(), size.height.toInt())
        )
    }
}

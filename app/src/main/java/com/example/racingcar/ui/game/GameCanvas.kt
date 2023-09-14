package com.example.racingcar.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.racingcar.ui.game.state.BackgroundState
import com.example.racingcar.ui.game.state.BlockersState
import com.example.racingcar.ui.game.state.CarState
import com.example.racingcar.ui.game.state.GameState


@Composable
fun GameCanvas(
    gameState: GameState,
    backgroundState: BackgroundState,
    backgroundSpeed: Int,
    blockersState: BlockersState,
    carState: CarState,
    carOffsetIndex: Float,
    onBlockerRectsDraw: (List<Rect>) -> Unit,
    onCarRectDraw: (Rect) -> Unit,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        drawBackground(
            gameState = gameState,
            backgroundState = backgroundState,
            backgroundSpeed = backgroundSpeed
        )

        drawBlockers(
            gameState = gameState,
            blockersState = blockersState,
            backgroundSpeed = backgroundSpeed,
            onDraw = onBlockerRectsDraw
        )

        drawCar(
            carState = carState,
            carOffsetIndex = carOffsetIndex,
            onDraw = onCarRectDraw
        )

    }
}

private fun DrawScope.drawBackground(
    gameState: GameState,
    backgroundState: BackgroundState,
    backgroundSpeed: Int
) {
    if (!gameState.isPaused())
        backgroundState.move(velocity = backgroundSpeed)
    backgroundState.draw(drawScope = this)
}

private fun DrawScope.drawBlockers(
    gameState: GameState,
    blockersState: BlockersState,
    backgroundSpeed: Int,
    onDraw: (List<Rect>) -> Unit,
) {
    if (!gameState.isStopped()) {
        if (!gameState.isPaused())
            blockersState.move(velocity = backgroundSpeed)
        val rects = blockersState.draw(drawScope = this)
        onDraw(rects)
    }
}

private fun DrawScope.drawCar(
    carState: CarState,
    carOffsetIndex: Float,
    onDraw: (Rect) -> Unit
) {
    val rect = carState.draw(drawScope = this, offsetIndex = carOffsetIndex)
    onDraw(rect)
}

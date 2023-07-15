package com.example.racingcar

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import com.example.racingcar.models.SwipeDirection
import com.example.racingcar.state.BackgroundState
import com.example.racingcar.state.BlockState
import com.example.racingcar.state.CarState
import kotlin.math.abs

@Composable
fun RacingCar(isDevMode: Boolean, modifier: Modifier = Modifier) {
    // resources
    val backgroundImageBitmap = ImageBitmap.imageResource(id = R.drawable.bg_road_night)
    val carImageBitmap = ImageBitmap.imageResource(id = R.drawable.ic_car)
    val blockImageBitmap = ImageBitmap.imageResource(id = R.drawable.ic_block_night)

    // states
    var gameScore by remember {
        mutableStateOf(0)
    }
    val backgroundSpeed by remember {
        derivedStateOf {
            (gameScore / Constants.GAME_SCORE_TO_VELOCITY_RATIO) + Constants.INITIAL_VELOCITY
        }
    }

    // animation
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    val ticker by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 250, easing = LinearEasing)
        ),
        label = "ticker"
    )

    val backgroundState =
        BackgroundState(image = backgroundImageBitmap, onGameScoreIncrease = { gameScore++ })
    val carState = CarState(image = carImageBitmap)
    val blockState = BlockState(image = blockImageBitmap)
    Log.d("mamad", "RacingCar: ")

    BoxWithConstraints(modifier = modifier) {
        var offsetX by remember { mutableStateOf(0f) }
        val minSwipeOffset by remember {
            mutableStateOf(
                constraints.maxWidth / 4
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            val (x, y) = dragAmount
                            offsetX += dragAmount.x
                        },
                        onDragEnd = {
                            when {
                                (offsetX < 0 && abs(offsetX) > minSwipeOffset) -> SwipeDirection.Left
                                (offsetX > 0 && abs(offsetX) > minSwipeOffset) -> SwipeDirection.Right
                                else -> null
                            }?.let { direction ->
                                carState.move(direction)
                            }

                            offsetX = 0F
                        }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                ticker

                backgroundState.move(velocity = backgroundSpeed)
                backgroundState.draw(drawScope = this)

                carState.draw(drawScope = this)

                blockState.move(velocity = backgroundSpeed)
                blockState.draw(drawScope = this)
            }

            Text(
                text = "score: $gameScore",
                modifier = Modifier.align(Alignment.TopCenter)
            )
            if (isDevMode)
                Button(onClick = { gameScore = 0 }) {
                    Text(text = "reset score")
                }
        }
    }

}

package com.example.racingcar.ui.game

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.example.racingcar.Constants
import com.example.racingcar.Constants.SWIPE_MIN_OFFSET_FROM_MAX_WIDTH
import com.example.racingcar.Constants.TICKER_ANIMATION_DURATION
import com.example.racingcar.MainViewModel
import com.example.racingcar.R
import com.example.racingcar.models.MovementInput.Accelerometer
import com.example.racingcar.models.MovementInput.Swipe
import com.example.racingcar.models.SwipeDirection
import com.example.racingcar.ui.game.state.BackgroundState
import com.example.racingcar.ui.game.state.BlockersState
import com.example.racingcar.ui.game.state.CarState
import com.example.racingcar.utils.vibrateError
import kotlin.math.abs


@Composable
fun RacingCar(
    viewModel: MainViewModel, //todo migrate to value state and expose events
    isDevMode: Boolean,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        viewModel.vibrateSharedFlow.collect {
            context.vibrateError()
        }
    }
    // resources
    val backgroundImageBitmap = ImageBitmap.imageResource(id = R.drawable.bg_road_night)
    val carImageBitmap = ImageBitmap.imageResource(id = R.drawable.ic_car)
    val blockImageBitmap = ImageBitmap.imageResource(id = R.drawable.ic_block_night)

    // states
    val gameScore by viewModel.gameScore.collectAsState()

    val backgroundSpeed by remember {
        derivedStateOf {
            (gameScore / Constants.GAME_SCORE_TO_VELOCITY_RATIO) + Constants.INITIAL_VELOCITY
        }
    }
    val backgroundState =
        BackgroundState(
            image = backgroundImageBitmap,
            onGameScoreIncrease = { viewModel.increaseGameScore() })
    val carState = CarState(image = carImageBitmap)

    val blockersState = BlockersState(image = blockImageBitmap)


    // ticker
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    val ticker by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = TICKER_ANIMATION_DURATION, easing = LinearEasing)
        ),
        label = "ticker"
    )

    BoxWithConstraints(modifier = modifier) {
        val acceleration by viewModel.acceleration.collectAsState()
        val movementInput by viewModel.movementInput.collectAsState()

        if (movementInput == Accelerometer)
            carState.moveWithAcceleration(acceleration)

        var offsetX by remember { mutableFloatStateOf(0f) }
        val minSwipeOffset by remember {
            mutableIntStateOf(constraints.maxWidth / SWIPE_MIN_OFFSET_FROM_MAX_WIDTH)
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    when (movementInput) {
                        Swipe -> Modifier.pointerInput(Unit) {
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

                        Accelerometer -> Modifier
                    }
                )
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                @Suppress("UNUSED_EXPRESSION") //todo enhance!
                ticker

                backgroundState.move(velocity = backgroundSpeed)
                backgroundState.draw(drawScope = this)

                blockersState.move(velocity = backgroundSpeed)
                val blockerRects = blockersState.draw(drawScope = this)

                val carRect = carState.draw(drawScope = this)

                val hasCollision = checkBlockerAndCarCollision(blockerRects, carRect)
                viewModel.updateCollision(hasCollision)
            }

            Text(
                text = "score: $gameScore",
                modifier = Modifier.align(Alignment.TopCenter)
            )
            if (isDevMode) {
                Button(onClick = { viewModel.resetGameScore() }) {
                    Text(text = "reset")
                }
            }
            Button(
                onClick = onSettingsClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Filled.Settings),
                    contentDescription = "settings"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Settings")
            }

        }
    }

}

fun checkBlockerAndCarCollision(blockerRects: List<Rect>, carRect: Rect): Boolean {
    return blockerRects.any { blockerRect ->
        blockerRect.overlaps(carRect)
    }
}

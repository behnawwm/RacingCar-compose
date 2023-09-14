package com.example.racingcar.ui.game

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import com.example.racingcar.R
import com.example.racingcar.models.MovementInput.Accelerometer
import com.example.racingcar.models.MovementInput.Gestures
import com.example.racingcar.ui.game.state.BackgroundState
import com.example.racingcar.ui.game.state.BlockersState
import com.example.racingcar.ui.game.state.CarState
import com.example.racingcar.ui.game.state.GameState
import com.example.racingcar.ui.viewmodel.MainViewModel
import com.example.racingcar.utils.Constants
import com.example.racingcar.utils.Constants.CAR_MOVEMENT_SPRING_ANIMATION_STIFFNESS
import com.example.racingcar.utils.Constants.TICKER_ANIMATION_DURATION

@Composable
fun RacingGameScreen(
    viewModel: MainViewModel, //todo migrate to value state and expose events
    isDevMode: Boolean,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // resources
    val backgroundImageBitmap = ImageBitmap.imageResource(id = R.drawable.bg_road_night)
    val carImageBitmap = ImageBitmap.imageResource(id = R.drawable.ic_car)
    val blockImageBitmap = ImageBitmap.imageResource(id = R.drawable.ic_block_night)

    // states
    val gameScore by viewModel.gameScore.collectAsState()
    val highscore by viewModel.highscore.collectAsState()
    var gameState by remember {
        mutableStateOf(GameState())
    }

    val backgroundSpeed by remember {
        derivedStateOf {
            (gameScore / Constants.GAME_SCORE_TO_VELOCITY_RATIO) + Constants.INITIAL_VELOCITY
        }
    }
    val backgroundState =
        BackgroundState(
            image = backgroundImageBitmap,
            onGameScoreIncrease = {
                if (gameState.isRunning())
                    viewModel.increaseGameScore()
            }
        )
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
        ticker //todo find a better way to put it in here!

        val acceleration by viewModel.acceleration.collectAsState()
        val movementInput by viewModel.movementInput.collectAsState()

        if (movementInput == Accelerometer)
            carState.moveWithAcceleration(acceleration)

        val carOffsetIndex by animateFloatAsState(
            targetValue = carState.position.fromLeftOffsetIndex(),
            label = "car offset index",
            animationSpec = spring(stiffness = CAR_MOVEMENT_SPRING_ANIMATION_STIFFNESS)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    when (movementInput) {
                        Gestures -> Modifier.then(
                            if (gameState.isRunning())
                                Modifier.detectCarPositionByPointerInput(maxWidth = maxWidth.value.toInt()) { position ->
                                    carState.moveWithTapGesture(position)
                                }
                            else Modifier
                        )

                        Accelerometer -> Modifier
                    }
                )
        ) {
            RacingGameCanvas(
                gameState = gameState,
                backgroundState = backgroundState,
                backgroundSpeed = backgroundSpeed,
                blockersState = blockersState,
                viewModel = viewModel,
                carState = carState,
                carOffsetIndex = carOffsetIndex,
                modifier = Modifier.fillMaxSize()
            )

            Text(
                text = "score: $gameScore",
                modifier = Modifier.align(Alignment.TopCenter)
            )
            Text(
                text = "high score: $highscore",
                modifier = Modifier.align(Alignment.TopStart)
            )

            androidx.compose.animation.AnimatedVisibility(
                visible = gameState.isStopped() || gameState.isPaused(),
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                GameStateIndicator(
                    gameState = gameState,
                    onStartClicked = { gameState = GameState(GameState.Status.RUNNING) }
                )
            }

        }
        TopActionButtons(
            onSettingsClick = onSettingsClick,
            onPauseGameState = { gameState = GameState(GameState.Status.PAUSED) },
            onResetGameScore = viewModel::resetGameScore,
            isDevMode = isDevMode,
            modifier = Modifier.fillMaxWidth()

        )
    }
}

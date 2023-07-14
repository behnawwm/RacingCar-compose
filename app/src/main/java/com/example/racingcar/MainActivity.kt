package com.example.racingcar

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.racingcar.ui.theme.RacingCarTheme

private const val BACKGROUND_VELOCITY = 50

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RacingCarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RacingCar(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }

    @Composable
    fun RacingCar(modifier: Modifier = Modifier) {
        // resources
        val backgroundImageBitmap = ImageBitmap.imageResource(id = R.drawable.bg_road_night)
        val carImageBitmap = ImageBitmap.imageResource(id = R.drawable.ic_car)

        // states
        var gameScore by remember {
            mutableStateOf(0)
        }
        val backgroundSpeed by remember {
            derivedStateOf {
                gameScore + 5
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

        val backgroundState = BackgroundState(image = backgroundImageBitmap)
        var carState by remember {
            mutableStateOf(CarState(image = carImageBitmap))
        }

        Box(modifier = modifier) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                ticker
                backgroundState.move(backgroundSpeed)
                backgroundState.draw(drawScope = this)

                carState.draw(drawScope = this, initialState = CarPosition.Left)
            }

            //todo state change not working
//            Button(onClick = {
//                carState.move(SwipeDirection.Left)
//            }) {
//                Text(text = "left")
//            }
//            Button(
//                onClick = { carState.move(SwipeDirection.Right) },
//                modifier = Modifier.align(Alignment.TopEnd)
//            ) {
//                Text(text = "right")
//            }

        }
    }
}
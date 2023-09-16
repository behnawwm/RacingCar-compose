package com.example.racingcar.ui.game

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.example.racingcar.ui.models.CarPosition
import com.example.racingcar.utils.Constants

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.detectCarPositionByPointerInput(
    maxWidth: Int,
    onDetectPosition: (CarPosition) -> Unit
) = Modifier.then(pointerInteropFilter { motionEvent ->
    val currentX = motionEvent.x

    val laneIndex = (currentX / maxWidth)
        .toInt()
        .coerceIn(0, Constants.LANE_COUNT)

    CarPosition.values()
        .find { position ->
            position
                .fromLeftOffsetIndex()
                .toInt() == laneIndex
        }?.let { position ->
            onDetectPosition(position)
        }

    true
})
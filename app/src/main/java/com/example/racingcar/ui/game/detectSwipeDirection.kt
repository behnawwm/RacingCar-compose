package com.example.racingcar.ui.game

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import com.example.racingcar.ui.models.SwipeDirection
import com.example.racingcar.utils.Constants
import kotlin.math.abs

fun Modifier.detectSwipeDirection(maxWidth: Int, onSwipeDirection: (SwipeDirection) -> Unit) =
    composed {

        var swipeOffsetX by remember { mutableFloatStateOf(0f) }
        val minSwipeOffset by remember {
            mutableIntStateOf(maxWidth / Constants.SWIPE_MIN_OFFSET_FROM_MAX_WIDTH)
        }

        Modifier.pointerInput(Unit) {
            detectDragGestures(
                onDrag = { change, dragAmount ->
                    change.consume()
                    swipeOffsetX += dragAmount.x
                },
                onDragEnd = {
                    when {
                        (swipeOffsetX < 0 && abs(swipeOffsetX) > minSwipeOffset) -> SwipeDirection.Left
                        (swipeOffsetX > 0 && abs(swipeOffsetX) > minSwipeOffset) -> SwipeDirection.Right
                        else -> null
                    }?.let { direction ->
                        onSwipeDirection(direction)
                    }

                    swipeOffsetX = 0F
                }
            )
        }
    }
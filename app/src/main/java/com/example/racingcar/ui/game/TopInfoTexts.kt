package com.example.racingcar.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TopInfoTexts(gameScore: () -> Int, highscore: () -> Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = "score: ${gameScore()}")
        Text(text = "high score: ${highscore()}")
    }
}


package com.example.racingcar.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp


@Composable
fun TopActionButtons(
    onSettingsClick: () -> Unit,
    onPauseGameState: () -> Unit,
    onResetGameScore: () -> Unit,
    isDevMode: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onSettingsClick,
        ) {
            Icon(
                painter = rememberVectorPainter(image = Icons.Filled.Settings),
                contentDescription = "settings"
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Settings")
        }
        Button(
            onClick = onPauseGameState,
            modifier = Modifier
        ) {
            Icon(
                painter = rememberVectorPainter(image = Icons.Filled.Pause),
                contentDescription = "pause"
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Pause")
        }
        if (isDevMode) {
            Button(onClick = onResetGameScore) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Filled.Refresh),
                    contentDescription = "reset"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Reset")
            }
        }
    }
}


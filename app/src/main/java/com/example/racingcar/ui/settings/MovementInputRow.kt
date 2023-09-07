package com.example.racingcar.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.racingcar.models.MovementInput

@Composable
fun MovementInputRow(
    movementInput: MovementInput,
    onMovementInputChange: (MovementInput) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Black)) {
                append("Movement Input: ")
            }
            withStyle(style = SpanStyle(color = Color.Red)) {
                append(movementInput.name)
            }

        }, color = Color.Black)
        Switch(
            checked = movementInput == MovementInput.Accelerometer,
            onCheckedChange = { isAccelerometer ->
                onMovementInputChange(
                    if (isAccelerometer)
                        MovementInput.Accelerometer
                    else
                        MovementInput.Gestures
                )
            },
        )
    }
}
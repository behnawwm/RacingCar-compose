package com.example.racingcar

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.racingcar.ui.theme.RacingCarTheme


class MainActivity : ComponentActivity(), SensorEventListener {
    private val viewModel by viewModels<MainViewModel>()

    private val sensorManager by lazy { getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val accelerometer by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RacingCarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    RacingCar(
                        viewModel = viewModel,
                        isDevMode = true,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //todo add toggle for turning off
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        Log.d("mamad", "onSensorChanged: x: ${event.values[0]}")
        // Get the acceleration along the X-axis
        val accelerationX = event.values[0]

        viewModel.setAccelerationX(accelerationX)


//        // Adjust the car's position based on the accelerometer data
//        carXPosition.value -= accelerationX * 2.0f // Adjust the sensitivity as needed
//
//        // Limit the car's movement within the screen boundaries (left and right lanes)
//        val screenWidth = resources.displayMetrics.widthPixels
//        if (carXPosition.value < 0) {
//            carXPosition.value = 0f
//        } else if (carXPosition.value > screenWidth) {
//            carXPosition.value = screenWidth.toFloat()
//        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
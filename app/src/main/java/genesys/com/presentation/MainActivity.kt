package genesys.com.presentation

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.MaterialTheme
import genesys.com.presentation.theme.GenesysTheme
import kotlinx.coroutines.*

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var heartRateSensor: Sensor? = null
    private var accelerometerSensor: Sensor? = null

    private var _heartRate by mutableStateOf(98f) // Initial heart rate is 98
    private var _accelX by mutableStateOf(0f)
    private var _accelY by mutableStateOf(0f)
    private var _accelZ by mutableStateOf(0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Start monitoring heart rate and accelerometer
        heartRateSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        accelerometerSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }

        setContent {
            WearApp(
                heartRate = _heartRate,
                accelX = _accelX,
                accelY = _accelY,
                accelZ = _accelZ
            )
        }
    }

    override fun onResume() {
        super.onResume()
        // Register listeners again when the app is resumed
        heartRateSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        accelerometerSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        // Unregister listeners when the app is paused
        sensorManager.unregisterListener(this)
    }

    // Handle sensor events for heart rate and accelerometer
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_HEART_RATE -> {
                _heartRate = event.values[0] // Update heart rate value
            }
            Sensor.TYPE_ACCELEROMETER -> {
                _accelX = event.values[0] // X axis accelerometer value
                _accelY = event.values[1] // Y axis accelerometer value
                _accelZ = event.values[2] // Z axis accelerometer value
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes if needed
    }

    @Composable
    fun WearApp(heartRate: Float, accelX: Float, accelY: Float, accelZ: Float) {
        GenesysTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Display Heart Rate
                        Text(
                            text = "Heart Rate: ${heartRate.toInt()} BPM",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary
                        )

                        if (heartRate > 100) {
                            Text(
                                text = "⚠ Warning: High Heart Rate!",
                                textAlign = TextAlign.Center,
                                color = Color.Red
                            )
                        }

                        // Display Accelerometer Data
                        Text(
                            text = "Accelerometer (X, Y, Z):\n" +
                                    "X: ${accelX.toInt()} " +
                                    "Y: ${accelY.toInt()} " +
                                    "Z: ${accelZ.toInt()} ",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.primary
                        )
                    }
                }
            }
        }
    }
}

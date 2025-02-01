package genesys.com.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.MaterialTheme
import genesys.com.presentation.sensors.AccelerometerSensor
import genesys.com.presentation.ui.HeartRateDisplay
import genesys.com.presentation.theme.GenesysTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var accelerometerSensor: AccelerometerSensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accelerometerSensor = AccelerometerSensor(this)

        setContent {
            GenesysTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black), // Set background color to black
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Display Heart Rate UI
                            HeartRateDisplay()

                            // Collect Accelerometer Data
                            val accelState = remember { mutableStateOf(Triple(0f, 0f, 0f)) }

                            LaunchedEffect(Unit) {
                                lifecycleScope.launch {
                                    accelerometerSensor.accelerationData.collectLatest {
                                        accelState.value = it
                                    }
                                }
                            }

                            // Display Accelerometer Data
                            Text(
                                text = "Accelerometer (X, Y, Z):\n" +
                                        "X: ${accelState.value.first.toInt()} " +
                                        "Y: ${accelState.value.second.toInt()} " +
                                        "Z: ${accelState.value.third.toInt()} ",
                                textAlign = TextAlign.Center,
                                color = Color.White // Set text color to white for contrast
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometerSensor.startListening()
    }

    override fun onPause() {
        super.onPause()
        accelerometerSensor.stopListening()
    }
}

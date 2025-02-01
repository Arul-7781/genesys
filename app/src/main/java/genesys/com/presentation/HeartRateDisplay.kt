package genesys.com.presentation.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.MaterialTheme
import kotlinx.coroutines.delay

@Composable
fun HeartRateDisplay() {
    var heartRate by remember { mutableStateOf(95f) }
    val animatedHeartRate by animateFloatAsState(
        targetValue = heartRate,
        animationSpec = tween(durationMillis = 500, easing = LinearEasing)
    )

    // Cycle heart rate between 95 and 107 with step of 2 over 3s
    LaunchedEffect(Unit) {
        while (true) {
            for (rate in 97..103 step 1) {
                heartRate = rate.toFloat()
                delay(2000) // Move to next value every 500ms
            }
        }
    }

    Column {
        // Display Heart Rate
        Text(
            text = "Heart Rate: ${animatedHeartRate.toInt()} BPM",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary
        )

        if (animatedHeartRate > 100) {
            Text(
                text = "⚠ Warning: High Heart Rate!",
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        }
    }
}

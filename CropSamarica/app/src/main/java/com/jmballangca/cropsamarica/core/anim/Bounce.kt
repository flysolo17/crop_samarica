package com.jmballangca.cropsamarica.core.anim

import android.graphics.drawable.Icon
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay


@Composable
fun BouncingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pressed by remember { mutableStateOf(false) }

    // Scale animation
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f, // shrink a bit when pressed
        animationSpec = tween(
            durationMillis = 100,
            easing = LinearOutSlowInEasing
        ),
        label = "bounce"
    )

    Button(
        onClick = {
            pressed = true
            onClick()
        },
        modifier = modifier
            .padding(vertical = 8.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Create")
        }
    }

    // Reset pressed state after animation
    LaunchedEffect(pressed) {
        if (pressed) {
            delay(120) // wait for shrink
            pressed = false // trigger bounce back
        }
    }
}

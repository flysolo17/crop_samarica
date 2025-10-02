package com.jmballangca.cropsamarica.core.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.shimmer(
    shimmerColors: List<Color> = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    ),
    durationMillis: Int = 1000,
    shimmering: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp)
): Modifier {
    return if (shimmering) {
        val transition = rememberInfiniteTransition(label = "shimmer")
        val translateAnim by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f, // large enough to cover width
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "shimmerAnim"
        )

        this.then(
            Modifier.drawWithContent {
                val brush = Brush.linearGradient(
                    colors = shimmerColors,
                    start = Offset(translateAnim - size.width, 0f),
                    end = Offset(translateAnim, size.height)
                )

                // Create outline from the given shape
                val outline = shape.createOutline(size, layoutDirection, this)

                when (outline) {
                    is Outline.Rectangle -> drawRect(brush = brush)
                    is Outline.Rounded -> drawRoundRect(
                        brush = brush,
                        cornerRadius = outline.roundRect.topLeftCornerRadius
                    )
                    is Outline.Generic -> drawPath(
                        path = outline.path,
                        brush = brush
                    )
                }
            }
        )
    } else {
        this // no shimmer
    }
}

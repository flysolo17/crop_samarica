package com.jmballangca.cropsamarica.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.presentation.RiceFieldDateTimeConverter

@Composable
fun RiceStageProgress(
    plantedDate: Long,
    expectedHarvestDate: Long,
    currentStage: RiceStage,
    modifier: Modifier = Modifier
) {
    val totalDays = 120
    val riceFieldDateTimeConverter = remember {
        RiceFieldDateTimeConverter(plantedDate, expectedHarvestDate)
    }
    val elapsed = riceFieldDateTimeConverter.getElapsedDays()

    val progress = (elapsed.toFloat() / totalDays).coerceIn(0f, 1f)

    val stages = listOf(
        RiceStage.SEEDLING to 15,
        RiceStage.TILLERING to 45,
        RiceStage.STEM_ELONGATION to 60,
        RiceStage.PANICLE_INITIATION to 75,
        RiceStage.BOOTING to 85,
        RiceStage.FLOWERING to 95,
        RiceStage.MILKING to 105,
        RiceStage.DOUGH to 115,
        RiceStage.MATURE to 120
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Current Stage: ${currentStage.name}",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(12.dp))

        // make the whole bar + markers scrollable
        Box(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            val totalWidth = 1000.dp // long enough for all stages
            Column {
                // Continuous progress bar
                Box(
                    modifier = Modifier
                        .width(totalWidth)
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.LightGray)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(totalWidth * progress)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Stage markers positioned along the bar
                Row(
                    modifier = Modifier.width(totalWidth),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    stages.forEach { (stage, day) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (elapsed >= day) MaterialTheme.colorScheme.primary
                                        else Color.Gray
                                    )
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = stage.name,
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Day $day",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}


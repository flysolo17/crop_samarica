package com.jmballangca.cropsamarica.presentation.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.domain.utils.displayDate
import com.jmballangca.cropsamarica.domain.utils.toYmdPht
import com.jmballangca.cropsamarica.presentation.RiceFieldDateTimeConverter

import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme
import java.util.Date



@Composable
fun RiceFieldCard(
    modifier: Modifier = Modifier,
    riceField: RiceField
) {
    val riceFieldDateTimeConverter = remember {
        RiceFieldDateTimeConverter(
            plantedDate = riceField.plantedDate,
            expectedHarvestDate = riceField.expectedHarvestDate
                ?: System.currentTimeMillis()
        )
    }
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = riceField.stage.name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "${riceField.areaSize} ha , ${riceField.variety}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = riceFieldDateTimeConverter.displayDate(),
                style = MaterialTheme.typography.bodyMedium
            )
            RiceStageProgress(
                plantedDate = riceField.plantedDate,
                expectedHarvestDate = riceField.expectedHarvestDate ?: System.currentTimeMillis(),
                currentStage = riceField.stage
            )
//            Text(
//                "PlantedDate : ${riceField.plantedDate.toYmdPht()}"
//            )
//            LinearProgressIndicator(
//                progress = { riceFieldDateTimeConverter.getDaysLeftPercentage() / 100 },
//                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//                color = ProgressIndicatorDefaults.linearColor,
//                trackColor = ProgressIndicatorDefaults.linearTrackColor,
//                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
//            )

        }

    }

}

@Preview
@Composable
private fun RiceFieldCardPrev() {
    CropSamaricaTheme {

    }
}
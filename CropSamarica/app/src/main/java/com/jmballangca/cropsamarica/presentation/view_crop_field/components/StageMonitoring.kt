package com.jmballangca.cropsamarica.presentation.view_crop_field.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme


@Composable
fun StageMonitoring(
    modifier: Modifier = Modifier,
    stage: RiceStage,
    task: List<String>,
    reminders: List<String>
) {
    var selectedStage by remember { mutableStateOf(stage) }
    val stages = RiceStage.entries
    Column(
        modifier = modifier.fillMaxWidth(),
    ){
        stages.forEach { stage ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (selectedStage == stage) {
                        Image(
                            painter = painterResource(id = stage.icon),
                            contentDescription = stage.name,
                            modifier = Modifier.size(42.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = stage.icon),
                            contentDescription = stage.name,
                            modifier = Modifier.size(42.dp)
                        )
                    }
                    if (selectedStage == stage) {
                        VerticalDivider(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text("${stage.name}", style = MaterialTheme.typography.titleMedium)

                }


            }
        }



    }
}

@Preview(showBackground = true)
@Composable
private fun StageMonitoringPrev() {
    CropSamaricaTheme {
        StageMonitoring(
            stage = RiceStage.TILLERING,
            task = emptyList(),
            reminders = emptyList()
        )
    }
}
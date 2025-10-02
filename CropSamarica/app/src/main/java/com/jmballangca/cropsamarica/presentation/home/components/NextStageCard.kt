package com.jmballangca.cropsamarica.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.RiceVariety
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme


@Composable
fun NextStageCard(
    modifier: Modifier = Modifier,
    stage: RiceStage,
    variety: RiceVariety,
    onNextStage: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text("Next Stage", style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            ))
            Text("Your crop is ready for next stage", style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(
            onClick = onNextStage,
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next Stage"
            )
        }

    }

}

@Preview
@Composable
private fun NextStagePrev() {
    CropSamaricaTheme {
        NextStageCard(

            stage = RiceStage.SEEDLING,
            variety = RiceVariety.NSIC_RC9,
            onNextStage = {}
        )
    }

}
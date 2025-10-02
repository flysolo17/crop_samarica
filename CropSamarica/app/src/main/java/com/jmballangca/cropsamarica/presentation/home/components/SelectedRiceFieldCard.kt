package com.jmballangca.cropsamarica.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.R
import com.jmballangca.cropsamarica.core.utils.shimmer
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField


@Composable
fun SelectedRiceFieldCard(
    modifier: Modifier = Modifier,
    selectedRiceField: RiceField?,
    onClick : () -> Unit
) {
    Card(
        modifier = modifier.clickable {
            onClick()
        },
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(selectedRiceField?.name ?: "No field selected", style = MaterialTheme.typography.titleSmall)
        }

    }

}
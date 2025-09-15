package com.jmballangca.cropsamarica.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.R
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField


@Composable
fun SelectedRiceFieldCard(
    modifier: Modifier = Modifier,
    selectedRiceField: RiceField,
    onClick : () -> Unit
) {
    Card(
        modifier = Modifier.clickable {
            onClick()
        },
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(selectedRiceField.name, style = MaterialTheme.typography.titleSmall)
        }

    }

}
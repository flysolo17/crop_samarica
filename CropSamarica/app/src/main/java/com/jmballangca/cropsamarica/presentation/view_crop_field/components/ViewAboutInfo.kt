package com.jmballangca.cropsamarica.presentation.view_crop_field.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.presentation.view_crop_field.RiceFieldDetailScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAboutInfo(
    modifier: Modifier = Modifier,
    riceField: RiceField
) {
    var expanded by remember { mutableStateOf(false) }
    if (expanded) {
        ModalBottomSheet(
            onDismissRequest = { expanded = !expanded },
            modifier = modifier
        ) {
            RiceFieldDetailScreen(riceField = riceField)
        }
    }
    IconButton(
        onClick = { expanded = !expanded },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = "Info"
        )
    }

}
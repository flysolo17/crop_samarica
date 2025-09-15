package com.jmballangca.cropsamarica.presentation.create_crop_field.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoilTypeSelector(
    modifier: Modifier = Modifier,
    selected: SoilTypes?,
    onSelected: (SoilTypes) -> Unit
) {
    val methods = SoilTypes.entries.toTypedArray()
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected?.label ?: "--Select Soil Type--",
            onValueChange = {
            },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = TextFieldDefaults.colors(),
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            methods.forEach { method ->
                DropdownMenuItem(
                    text = {
                        ListItem(
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent,
                            ),
                            overlineContent = {
                                AssistChip(
                                    colors = when (method.suitability) {
                                        Suitability.HIGH -> AssistChipDefaults.assistChipColors(
                                            containerColor = Color(0xFFD0F0C0), // light green
                                            labelColor = Color(0xFF1B5E20)      // dark green text
                                        )
                                        Suitability.MEDIUM -> AssistChipDefaults.assistChipColors(
                                            containerColor = Color(0xFFFFF9C4), // light yellow
                                            labelColor = Color(0xFFF57F17)      // dark yellow text
                                        )
                                        Suitability.LOW -> AssistChipDefaults.assistChipColors(
                                            containerColor = Color(0xFFFFCDD2), // light red
                                            labelColor = Color(0xFFB71C1C)      // dark red text
                                        )
                                    },
                                    onClick = { },
                                    label = { Text(
                                        method.suitability.name,
                                        style = MaterialTheme.typography.labelSmall
                                    ) }
                                )
                            },
                            headlineContent = { Text(method.label) },
                            supportingContent = { Text(method.description,style = MaterialTheme.typography.bodySmall) }
                        )
                    },
                    onClick = {
                        onSelected(method)
                        expanded = false
                    }
                )
            }
        }

    }
}

@Preview
@Composable
private fun SoilTypeSelectorPrev() {
    CropSamaricaTheme {
        SoilTypeSelector(
            selected = null,
            onSelected = {}
        )
    }
}
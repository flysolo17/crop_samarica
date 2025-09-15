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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiceVarietySelector(
    modifier: Modifier = Modifier,
    selected: RiceVariety?,
    onSelected: (RiceVariety) -> Unit
) {
    val varieties = RiceVariety.entries.toTypedArray()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected?.displayName ?: "",
            onValueChange = { },
            label = { Text("Select Variety") },
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
            varieties.forEach { variety ->
                DropdownMenuItem(
                    text = {
                        ListItem(
                            colors = ListItemDefaults.colors(
                                containerColor = Color.Transparent,
                            ),
                            overlineContent = {
                                AssistChip(
                                    colors = when (variety.environment) {
                                        RiceVariety.EnvironmentType.IRRIGATED -> AssistChipDefaults.assistChipColors(
                                            containerColor = Color(0xFFD0F0C0), // light green
                                            labelColor = Color(0xFF1B5E20)      // dark green
                                        )
                                        RiceVariety.EnvironmentType.RAINFED -> AssistChipDefaults.assistChipColors(
                                            containerColor = Color(0xFFFFF9C4), // light yellow
                                            labelColor = Color(0xFFF57F17)      // dark yellow
                                        )
                                        RiceVariety.EnvironmentType.UPLAND -> AssistChipDefaults.assistChipColors(
                                            containerColor = Color(0xFFFFE0B2), // light orange
                                            labelColor = Color(0xFFE65100)      // dark orange
                                        )
                                        RiceVariety.EnvironmentType.HYBRID -> AssistChipDefaults.assistChipColors(
                                            containerColor = Color(0xFFBBDEFB), // light blue
                                            labelColor = Color(0xFF0D47A1)      // dark blue
                                        )
                                        RiceVariety.EnvironmentType.UNKNOWN -> AssistChipDefaults.assistChipColors(
                                            containerColor = Color.LightGray,
                                            labelColor = Color.DarkGray
                                        )
                                    },
                                    onClick = { },
                                    label = { Text(
                                        variety.environment.name,
                                        style = MaterialTheme.typography.labelSmall
                                    ) }
                                )
                            },
                            headlineContent = { Text(variety.displayName) },
                            supportingContent = {
                                Text(
                                    "${variety.description}\n⏳ ${variety.maturityDays.first}-${variety.maturityDays.last} days",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        )
                    },
                    onClick = {
                        onSelected(variety)
                        expanded = false
                    }
                )
            }
        }
    }
}

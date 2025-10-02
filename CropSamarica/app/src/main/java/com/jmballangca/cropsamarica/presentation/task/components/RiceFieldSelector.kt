package com.jmballangca.cropsamarica.presentation.task.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiceFieldSelector(
    modifier: Modifier = Modifier,
    selectedRiceField: RiceField,
    riceFields: List<RiceField>,
    onSelected: (RiceField) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(),
            value = selectedRiceField.name,
            onValueChange = {},
            label = {
                androidx.compose.material3.Text("Select Rice Field")
            },
            trailingIcon = {
                androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            readOnly = true,
            modifier = modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            riceFields.forEach { riceField->
                androidx.compose.material3.DropdownMenuItem(
                    text = {
                        androidx.compose.material3.Text(text = riceField.name)
                    },
                    onClick = {
                        onSelected(riceField)
                        expanded = false
                    }
                )
            }
        }

    }
}

@Preview
@Composable
private fun RiceFieldSelectorPreview() {
    CropSamaricaTheme {
        RiceFieldSelector(
            selectedRiceField = RiceField(
                name = "Rice Field 1",
                id = "1"
            ),
            riceFields = listOf(
                RiceField(
                    name = "Rice Field 2"),
                RiceField(
                    name = "Rice Field 3")
            ),
            onSelected = {}
        )
    }

}
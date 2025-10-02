package com.jmballangca.cropsamarica.presentation.task.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.jmballangca.cropsamarica.data.models.task.TaskStatus
import com.jmballangca.cropsamarica.data.models.task.getBackgroundColor
import com.jmballangca.cropsamarica.data.models.task.getTextColor
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusSelector(
    modifier: Modifier = Modifier,
    status: TaskStatus,
    onStatusChange: (TaskStatus) -> Unit
) {
    val options = TaskStatus.entries
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            value = status.title,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(),
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )

        DropdownMenu(
            expanded = expanded,
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = { expanded = !expanded},
        ) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it.title) },
                    onClick = {
                        onStatusChange(it)
                        expanded = !expanded
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun StatusSelectorPrev() {
    CropSamaricaTheme {
        StatusSelector(
            status = TaskStatus.PENDING,
            onStatusChange = {}
        )
    }
}
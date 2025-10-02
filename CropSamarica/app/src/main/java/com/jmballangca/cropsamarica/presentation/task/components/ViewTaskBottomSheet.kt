package com.jmballangca.cropsamarica.presentation.task.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.core.ui.CropSamaricaDatePicker
import com.jmballangca.cropsamarica.core.utils.UIState
import com.jmballangca.cropsamarica.data.models.task.Task
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTaskBottomSheet(
    modifier: Modifier = Modifier,
    task : Task,
    onDismiss : () -> Unit,
    onEdit: (Task) -> Unit,
    onDelete : (id : String) -> Unit,
) {

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
    ) {
        TaskBottomSheetForm(
            task = task,
            onEdit = { task ->
                onEdit(task)
                onDismiss()
            },
            onDelete = { id ->
                onDelete(id)
                onDismiss()
            }
        )

    }
}

@Composable
fun TaskBottomSheetForm(
    modifier: Modifier = Modifier,
    task : Task,
    onEdit: (Task) -> Unit,
    onDelete : (id : String) -> Unit,
) {

    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var startDate by remember { mutableStateOf(task.startDate) }
    var dueDate by remember { mutableStateOf(task.dueDate) }
    var status by remember { mutableStateOf(task.status) }
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Edit Task",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(
            modifier = Modifier.padding(8.dp)
        )
        OutlinedTextField(
            value = title,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(),
            singleLine = true,
            isError = title.isEmpty(),
            supportingText = {
                if (title.isEmpty()) {
                    Text(text = "Title is required")
                }
            },
            onValueChange = {
                title = it
            },

            placeholder = {
                Text("Title")
            }
        )
        StatusSelector(
            status = status,
            onStatusChange = {
                status = it
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CropSamaricaDatePicker(
                title = "Start Date",
                selected = startDate,
                onSelected = {
                    startDate = it
                },
                modifier = Modifier.weight(1f)
            )
            CropSamaricaDatePicker(
                title = "Due Date",
                selected = dueDate,
                minDate = startDate,
                onSelected = {
                    dueDate = it
                },
                modifier = Modifier.weight(1f)
            )
        }
        OutlinedTextField(
            value = description,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            textStyle = MaterialTheme.typography.bodyMedium,
            minLines = 3,
            onValueChange = {
                description = it
            },
            label = {
                Text("Description (Optional)")
            }
        )
        Spacer(
            modifier = Modifier.padding(8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)

        ) {
            Button(
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    onDelete(task.id)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )

            ) {
                Text("Delete")
            }
            Button(
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium,
                enabled = title.isNotEmpty(),
                onClick = {
                    val newTask = task.copy(
                        title = title,
                        description = description,
                        startDate = startDate,
                        dueDate = dueDate,
                        status = status
                    )
                    onEdit(newTask)
                }
            ) {
                Text("Save")
            }
        }

    }
}

@Preview(
    showBackground = true
)
@Composable
private fun TaskFormPrev() {
    CropSamaricaTheme {
        TaskBottomSheetForm(
            task = Task(
                title = "Sample Task",
                description = "This is a sample task",
                startDate = null,
                dueDate = null,
                status = com.jmballangca.cropsamarica.data.models.task.TaskStatus.PENDING
            ),
            onEdit = { _,-> },
            onDelete = { _,-> }
        )
    }

}
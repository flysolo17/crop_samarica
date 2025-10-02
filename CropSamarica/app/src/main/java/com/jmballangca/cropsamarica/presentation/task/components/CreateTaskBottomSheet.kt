package com.jmballangca.cropsamarica.presentation.task.components

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.core.ui.CropSamaricaDatePicker
import com.jmballangca.cropsamarica.core.utils.UIState
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.task.Task
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskBottomSheet(
    modifier: Modifier = Modifier,
    selectedRiceField : RiceField,
    riceFields : List<RiceField>,
    onSave : (Task,result : (UIState<String>) -> Unit) -> Unit,
) {
    var open by remember { mutableStateOf(false) }
    var title by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    var startDate by remember { mutableStateOf<Date?>(null) }
    var dueDate by remember { mutableStateOf<Date?>(null) }
    var selectedRiceField by remember { mutableStateOf(selectedRiceField) }
    var isLoading by remember { mutableStateOf(false) }
    LaunchedEffect(startDate) {
        if (startDate != null && dueDate != null) {
            if (startDate!!.after(dueDate)) {
                dueDate = startDate
            }
        }
    }
    if (open) {
        ModalBottomSheet(
            onDismissRequest = {
                open = !open
            },

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Create Task",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(
                    modifier = Modifier.padding(16.dp)
                )
                OutlinedTextField(
                    value = title,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
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

                    label = {
                        Text("Title")
                    }
                )
                RiceFieldSelector(
                    selectedRiceField = selectedRiceField,
                    riceFields = riceFields,
                    onSelected = {
                        selectedRiceField = it
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
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    enabled = title.isNotEmpty() && !isLoading,
                    onClick = {
                        val task = Task(
                            title = title,
                            description = description,
                            startDate = startDate,
                            dueDate = dueDate,
                            stage = selectedRiceField.stage,
                            fieldId = selectedRiceField.id
                        )
                        onSave(task) {
                            when(it) {
                                is UIState.Loading -> {
                                    isLoading = true
                                }
                                is UIState.Success -> {
                                    isLoading = false
                                    title = ""
                                    description = ""
                                    startDate = null
                                    dueDate = null
                                    selectedRiceField = riceFields[0]
                                    open = false
                                }
                                is UIState.Error -> {
                                    isLoading = false
                                }
                            }
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier.padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                            )
                        } else {
                            Text("Create Task")
                        }

                    }

                }
            }

        }

    }

    IconButton(
        onClick = {
            open = !open
        }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Task"
        )
    }

}
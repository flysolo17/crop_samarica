package com.jmballangca.cropsamarica.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.core.utils.UIState
import com.jmballangca.cropsamarica.core.utils.showToast
import com.jmballangca.cropsamarica.data.models.task.Task


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTask(
    modifier: Modifier = Modifier,
    task: Task,
    onClose: () -> Unit = {},
    onEditTask: (task: Task, result : (UIState<String>) -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    if (expanded) {
        ModalBottomSheet(
            onDismissRequest = {
                expanded = !expanded
            },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Edit Task",style = MaterialTheme.typography.titleMedium)
                Spacer(
                    modifier = Modifier.padding(8.dp)
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
                OutlinedTextField(
                    value = description,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    minLines = 3,
                    onValueChange = {
                        description = it
                    },
                    label = {
                        Text("Description")
                    }
                )

                Spacer(
                    modifier = Modifier.padding(8.dp)
                )
                Button(
                    enabled = title.isNotEmpty() && !loading,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val newTask = task.copy(
                            title = title,
                            description = description
                        )
                        onEditTask(newTask) {
                            when(it) {
                                is UIState.Error -> {
                                    loading = false
                                    expanded = false
                                    context.showToast(it.message)
                                    onClose()
                                }
                                UIState.Loading -> {loading = true}
                                is UIState.Success<String> -> {
                                    loading = false
                                    expanded = false
                                    context.showToast(it.data)
                                    onClose()
                                }
                            }
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier.padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text("Save Changes")
                        }
                    }

          
                }
            }
        }
    }

    Button(
        onClick = {
            expanded = !expanded
        },
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text("Edit")
    }
}
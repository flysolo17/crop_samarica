package com.jmballangca.cropsamarica.presentation.task

import com.jmballangca.cropsamarica.data.models.task.Task

data class TaskState(
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val error: String? = null,
    val selectedIndex : Int = 0,
    val selectedTask : Task? = null,
    val isCreatingTask : Boolean = false
)
package com.jmballangca.cropsamarica.presentation.task

import com.jmballangca.cropsamarica.core.utils.UIState
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.task.Task

sealed interface TaskEvent {
    data class LoadTask(
        val riceField : List<RiceField>
    ) : TaskEvent

    data class OnSelected(
        val index : Int
    ) : TaskEvent

    data class OnCreateTask(
        val task : Task,
        val result : (UIState<String>) -> Unit
    ) : TaskEvent

    data class OnDeleteTask(
        val taskId : String,
    ) : TaskEvent
    data class OnUpdateTask(
        val task : Task,
    ) : TaskEvent
    data class OnTaskSelected(
        val task : Task?
    ) : TaskEvent
}
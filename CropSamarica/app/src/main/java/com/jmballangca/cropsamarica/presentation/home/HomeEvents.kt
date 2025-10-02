package com.jmballangca.cropsamarica.presentation.home

import com.jmballangca.cropsamarica.core.utils.UIState
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.task.Task
import com.jmballangca.cropsamarica.data.models.task.TaskStatus
import java.util.Date


sealed interface HomeEvents {

    data class GetRiceField(
        val riceFieldId: String
    ) : HomeEvents

    data class OnSelectTaskStatus(
        val taskStatus: TaskStatus
    ) : HomeEvents

    data class OnChangeTaskStatus(
        val id : String,
        val taskStatus: TaskStatus,
        val result : (UIState<String>) -> Unit
    ) : HomeEvents



    data class OnCreateTask(
        val title : String,
        val description : String,
        val startDate : Date? = null,
        val dueDate : Date? = null,
        val result : (UIState<String>) -> Unit
    ) : HomeEvents



}
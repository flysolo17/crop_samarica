package com.jmballangca.cropsamarica.domain.repository

import com.jmballangca.cropsamarica.core.utils.UIState
import com.jmballangca.cropsamarica.data.models.task.Task
import com.jmballangca.cropsamarica.data.models.task.TaskStatus
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun insert(task: Task): Result<String>
    suspend fun create(task: Task, result: (UIState<String>) -> Unit)
    suspend fun statusChange(id : String, status: TaskStatus, result: (UIState<String>) -> Unit)
    suspend fun update(task: Task,result: (UIState<String>) -> Unit)
    suspend fun delete(id : String,result: (UIState<String>) -> Unit)
    suspend fun insertAll(tasks: List<Task>): Result<String>
    fun getByFieldId(fieldId: String): Flow<List<Task>>

    fun getAllByFieldIds(
        fieldIds: List<String>
    ) : Flow<List<Task>>
}
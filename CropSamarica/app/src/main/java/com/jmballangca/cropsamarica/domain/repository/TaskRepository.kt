package com.jmballangca.cropsamarica.domain.repository

import com.jmballangca.cropsamarica.core.utils.Reminder
import com.jmballangca.cropsamarica.core.utils.UIState
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.task.RiceFieldTask
import com.jmballangca.cropsamarica.data.models.task.Task
import com.jmballangca.cropsamarica.data.models.task.TaskStatus
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun insert(task: Task): Result<String>
    suspend fun create(task: Task, result: (UIState<String>) -> Unit)
    suspend fun statusChange(id : String, status: TaskStatus, result: (UIState<String>) -> Unit)
    suspend fun update(task: Task) : Result<String>
    suspend fun delete(id : String) : Result<String>
    suspend fun insertAll(tasks: List<Task>): Result<String>
    fun getByFieldId(fieldId: String): Flow<List<Task>>

    fun getAllByFieldIds(
        fieldIds: List<String>
    ) : Flow<List<Task>>

    fun getAll(
        riceField : List<RiceField>
    ): Flow<List<Task>>


    fun getRemindersToday(
        id : String
    ) : Flow<List<Reminder>>


    suspend fun createReminders(
        reminder: Reminder
    ) : Result<String>


    fun getTasksByFieldId(fieldId: String): Flow<List<Task>>

    fun getRemindersByFieldId(fieldId: String): Flow<List<Reminder>>


    fun getTodayReminder(
        id : String
    ) : Flow<List<Reminder>>

}
package com.jmballangca.cropsamarica.domain.repository.impl

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jmballangca.cropsamarica.core.utils.Reminder
import com.jmballangca.cropsamarica.core.utils.UIState
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.task.Task
import com.jmballangca.cropsamarica.data.models.task.TaskStatus
import com.jmballangca.cropsamarica.domain.repository.TaskRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: com.google.firebase.auth.FirebaseAuth,

) : TaskRepository {
    private val taskRef = firestore.collection("tasks")
    private val reminderRef = firestore.collection("reminders")

    override suspend fun insert(task: Task): Result<String> {
        return try {
            task.id = taskRef.document().id
            taskRef.document(task.id).set(task).await()
            Result.success("Task inserted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun create(
        task: Task,
        result: (UIState<String>) -> Unit
    ) {
        val uid = auth.currentUser?.uid
        if (uid.isNullOrEmpty()) {
            result.invoke(UIState.Error("User not found"))
            return
        }
        result.invoke(UIState.Loading)
        val id = taskRef.document().id
        task.id = id
        task.uid = uid
        taskRef.document(id).set(task).addOnCompleteListener {
            result.invoke(
                UIState.Success("Task created successfully")
            )
        }.addOnFailureListener {
            result.invoke(
                UIState.Error(it.message ?: "Unknown error")
            )
        }
    }

    override suspend fun statusChange(
        id: String,
        status: TaskStatus,
        result: (UIState<String>) -> Unit
    ) {
        result.invoke(UIState.Loading)
        taskRef.document(id).update("status", status).addOnCompleteListener {
            result.invoke(
                UIState.Success("Task status updated successfully")
            )
        }.addOnFailureListener {
            result.invoke(
                UIState.Error(it.message ?: "Unknown error")
            )
        }
    }

    override suspend fun update(task: Task): Result<String> {
        return try {
            taskRef.document(task.id).set(task).await()
            Result.success("Task updated successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun delete(id: String): Result<String> {
        return try {
            taskRef.document(id).delete().await()
            Result.success("Task deleted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun insertAll(tasks: List<Task>): Result<String> {
        return try {
            val batch = firestore.batch()

            tasks.forEach { task ->
                val taskId = taskRef.document().id
                val taskWithId = task.copy(id = taskId)
                batch.set(taskRef.document(taskId), taskWithId)
            }

            batch.commit().await()
            Result.success("Tasks inserted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override fun getByFieldId(fieldId: String): Flow<List<Task>> {
        return callbackFlow {
            val listener = taskRef
                .whereEqualTo("fieldId", fieldId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(emptyList())
                    } else {
                        val tasks = snapshot?.toObjects(Task::class.java) ?: emptyList()
                        trySend(tasks)
                    }
                }
            awaitClose {
                listener.remove()

            }
        }
    }

    override fun getAllByFieldIds(fieldIds: List<String>): Flow<List<Task>> {
        return callbackFlow {
            val listener = taskRef
                .whereIn("fieldId", fieldIds)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(emptyList())
                    } else {
                        val tasks = snapshot?.toObjects(Task::class.java) ?: emptyList()
                        trySend(tasks)
                    }
                }
            awaitClose {
                listener.remove()
            }
        }
    }

    override fun getAll(riceField: List<RiceField>): Flow<List<Task>> {
        return callbackFlow {
            val fieldIds = riceField.map { it.id }
            val listener = taskRef
                .whereIn("fieldId", fieldIds)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(emptyList())
                        return@addSnapshotListener
                    }
                    val tasks = snapshot?.toObjects(Task::class.java) ?: emptyList()
                    val riceFieldTasks = tasks.filter {
                        it.fieldId in fieldIds
                    }

                    trySend(riceFieldTasks)
                }
            awaitClose {
                listener.remove()
            }
        }
    }

    override fun getRemindersToday(id: String): Flow<List<Reminder>> = callbackFlow {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = Timestamp(calendar.time)

        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        val endOfDay = Timestamp(calendar.time)

        val listener = reminderRef
            .whereEqualTo("riceFieldId", id)
            .whereGreaterThanOrEqualTo("createdAt", startOfDay)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("TaskRepositoryImpl", "getRemindersToday error: ", error)
                    trySend(emptyList()).isSuccess
                    return@addSnapshotListener
                }

                val reminders = snapshot?.toObjects(Reminder::class.java).orEmpty()
                trySend(reminders).isSuccess
            }

        awaitClose { listener.remove() }
    }

    override suspend fun createReminders(reminder: Reminder): Result<String> {
        return try {
            val uid = auth.currentUser?.uid ?: throw IllegalStateException("User not found")
            reminder.id = reminderRef.document().id
            reminder.uid = uid
            reminderRef.document(reminder.id).set(reminder).await()
            Result.success("Task inserted successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getTasksByFieldId(fieldId: String): Flow<List<Task>> {
        return callbackFlow {
            val listener = taskRef
                .whereEqualTo("fieldId", fieldId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(emptyList())
                    } else {
                        val tasks = snapshot?.toObjects(Task::class.java) ?: emptyList()
                        trySend(tasks)
                    }
                }
            awaitClose {
                listener.remove()
            }
        }
    }

    override fun getRemindersByFieldId(fieldId: String): Flow<List<Reminder>> {
        return callbackFlow {
            val listener = reminderRef
                .whereEqualTo("riceFieldId", fieldId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(emptyList())
                    } else {
                        val reminders = snapshot?.toObjects(Reminder::class.java) ?: emptyList()
                        trySend(reminders)
                    }
                }
            awaitClose {
                listener.remove()
            }
        }
    }

    override fun getTodayReminder(id: String) = callbackFlow {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = Timestamp(calendar.time)

        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        val endOfDay = Timestamp(calendar.time)

        val listener = reminderRef
            .whereEqualTo("riceFieldId", id)
            .whereGreaterThanOrEqualTo("reminderDate", startOfDay)
            .whereLessThanOrEqualTo("reminderDate", endOfDay)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("TaskRepositoryImpl", "getRemindersToday error: ", error)
                    trySend(emptyList()).isSuccess
                    return@addSnapshotListener
                }

                val reminders = snapshot?.toObjects(Reminder::class.java).orEmpty()
                trySend(reminders).isSuccess
            }
        awaitClose { listener.remove() }
    }

}
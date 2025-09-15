package com.jmballangca.cropsamarica.domain.repository.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jmballangca.cropsamarica.core.utils.UIState
import com.jmballangca.cropsamarica.data.models.task.Task
import com.jmballangca.cropsamarica.data.models.task.TaskStatus
import com.jmballangca.cropsamarica.domain.repository.TaskRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : TaskRepository {
    private val taskRef = firestore.collection("tasks")

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
        result.invoke(UIState.Loading)
        val id = taskRef.document().id
        task.id = id
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

    override suspend fun update(
        task: Task,
        result: (UIState<String>) -> Unit
    ) {
        result.invoke(UIState.Loading)
        taskRef.document(task.id).set(task).addOnCompleteListener {
            result.invoke(
                UIState.Success("Task updated successfully")
            )
        }.addOnFailureListener {
            result.invoke(
                UIState.Error(it.message ?: "Unknown error")
            )
        }
    }

    override suspend fun delete(
        id : String,
        result: (UIState<String>) -> Unit
    ) {
        result.invoke(UIState.Loading)
        taskRef.document(id).delete().addOnCompleteListener {
            result.invoke(
                UIState.Success("Task deleted successfully")
            )
        }.addOnFailureListener {
            result.invoke(
                UIState.Error(it.message ?: "Unknown error")
            )
        }
    }

    override suspend fun insertAll(tasks: List<Task>): Result<String> {
        return try {
            val batch = firestore.batch()

            tasks.forEach { task ->
                val taskId = taskRef.document().id
                val taskWithId = task.copy(id = taskId) // safer than mutating
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
}
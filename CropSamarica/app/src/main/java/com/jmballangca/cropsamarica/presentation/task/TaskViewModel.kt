package com.jmballangca.cropsamarica.presentation.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.core.utils.UIState
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.task.RiceFieldTask
import com.jmballangca.cropsamarica.data.models.task.Task
import com.jmballangca.cropsamarica.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject





@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private var _state = MutableStateFlow(TaskState())
    val state = _state.asStateFlow()
    private val _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()

    fun events(e : TaskEvent) {
        when(e) {
            is TaskEvent.LoadTask -> loadTask(e.riceField)
            is TaskEvent.OnSelected -> _state.value = _state.value.copy(selectedIndex = e.index)
            is TaskEvent.OnCreateTask -> createTask(e.task,e.result)
            is TaskEvent.OnDeleteTask -> deleteTask(e.taskId)
            is TaskEvent.OnUpdateTask -> updatetask(e.task)
            is TaskEvent.OnTaskSelected -> _state.value = _state.value.copy(selectedTask = e.task)
        }
    }

    private fun deleteTask(taskId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            taskRepository.delete(taskId).onSuccess {
                _state.value = _state.value.copy(isLoading = false)
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it))
                }.onFailure {
                _state.value = _state.value.copy(isLoading = false)
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it.message.toString()))
            }
        }
    }

    private fun updatetask(task: Task) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            taskRepository.update(task).onSuccess {
                _state.value = _state.value.copy(isLoading = false)
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it))

            }.onFailure {
                _state.value = _state.value.copy(isLoading = false)
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it.message.toString()))
            }
        }
    }

    private fun createTask(
        task: Task,
        result: (UIState<String>) -> Unit
    ): Job {
        return viewModelScope.launch {
            taskRepository.create(task, result)
        }
    }


    private fun loadTask(riceField: List<RiceField>) {
        taskRepository.getAll(riceField).onStart {
            _state.value = _state.value.copy(isLoading = true)
        }.onEach {
            _state.value = _state.value.copy(isLoading = false, tasks = it)
        }.launchIn(viewModelScope)
    }
}
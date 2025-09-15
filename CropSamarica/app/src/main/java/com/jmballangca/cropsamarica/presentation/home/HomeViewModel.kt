package com.jmballangca.cropsamarica.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.core.utils.UIState
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.data.models.task.Task
import com.jmballangca.cropsamarica.data.models.task.TaskStatus

import com.jmballangca.cropsamarica.domain.repository.AyaRepository
import com.jmballangca.cropsamarica.domain.repository.ForecastRepository
import com.jmballangca.cropsamarica.domain.repository.RiceFieldRepository
import com.jmballangca.cropsamarica.domain.repository.TaskRepository
import com.jmballangca.cropsamarica.presentation.navigation.SURVEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.emptyList





@HiltViewModel
class HomeViewModel @Inject constructor(
    private val riceFieldRepository: RiceFieldRepository,
    private val taskRepository: TaskRepository,
    private val ayaRepository: AyaRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
    private var _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()
    fun events(e : HomeEvents) {
        when(e) {
            is HomeEvents.GetRiceField -> getRiceField(
                riceFieldId = e.riceFieldId
            )

            is HomeEvents.OnSelectTaskStatus -> {
                _state.value = _state.value.copy(
                    selectedTaskStatus = e.taskStatus
                )
            }

            is HomeEvents.OnChangeTaskStatus -> changeStatus(
                id = e.id,
                taskStatus = e.taskStatus,
                result = e.result
            )

            is HomeEvents.OnDeleteTask -> deleteTask(
                id = e.id,
                result = e.result
            )

            is HomeEvents.OnEditTask -> editTask(
                task = e.task,
                result = e.result
            )

            is HomeEvents.OnCreateTask -> createTask(e.title, e.description, e.result)
        }
    }

    private fun createTask(
        title: String,
        description: String,
        result: (UIState<String>) -> Unit
    ):  Job {
        return viewModelScope.launch {
            val task = Task(
                title = title,

                description = description,
                status = TaskStatus.PENDING,
                stage = state.value.riceFieldWithWeather?.riceField?.stage ?: RiceStage.SEEDLING,
                fieldId = state.value.riceFieldWithWeather?.riceField?.id ?: ""
            )
            taskRepository.create(
                task = task,
                result = result
            )
        }
    }

    private fun editTask(
        task: Task,
        result: (UIState<String>) -> Unit
    ): Job {
        return viewModelScope.launch {
            taskRepository.update(
                task = task,
                result = result
            )
        }
    }

    private fun deleteTask(
        id: String,
        result: (UIState<String>) -> Unit
    ): Job {
        return viewModelScope.launch {
            taskRepository.delete(
                id = id,
                result = result
            )
        }
    }

    private fun changeStatus(
        id: String,
        taskStatus: TaskStatus,
        result: (UIState<String>) -> Unit
    ) : Job {
        return viewModelScope.launch {
            taskRepository.statusChange(
                id = id,
                status = taskStatus,
                result = result
            )
        }
    }


    private fun getRiceField(
        riceFieldId: String
    ) {
        riceFieldRepository.getRiceField(riceFieldId).onStart {
            _state.value = _state.value.copy(
                isLoading = true
            )
            delay(2000)
        }.onEach {
            _state.value = _state.value.copy(
                isLoading = false,
                riceFieldWithWeather = it
            )
            Log.d("Tasks", "getTasks: ${it.tasks}")
        }.launchIn(viewModelScope)
    }


}
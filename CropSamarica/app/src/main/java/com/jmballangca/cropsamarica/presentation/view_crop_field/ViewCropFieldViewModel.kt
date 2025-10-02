package com.jmballangca.cropsamarica.presentation.view_crop_field

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.core.utils.Reminder
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.data.models.task.Task
import com.jmballangca.cropsamarica.domain.repository.RiceFieldRepository
import com.jmballangca.cropsamarica.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ViewCropFieldState(
    val isLoading : Boolean = false,
    val riceField : RiceField ? = null,
    val tasks : List<Task> = emptyList(),
    val selectedTab : RiceStage = RiceStage.SEEDLING,
    val reminders : List<Reminder> = emptyList()
)

sealed interface ViewCropFieldEvents {
    data class GetRiceField(val riceFieldId : String) : ViewCropFieldEvents
    data class OnStageSelected(val stage : RiceStage) : ViewCropFieldEvents
    data class OnDeleteCrop(
        val id : String,
    ) : ViewCropFieldEvents
}

@HiltViewModel
class ViewCropFieldViewModel @Inject constructor(
    private val riceFieldRepository: RiceFieldRepository,
    private val taskRepository: TaskRepository
): ViewModel() {

    private var _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()
    private val _state = MutableStateFlow(ViewCropFieldState())
    val state = _state.asStateFlow()

    fun events(e : ViewCropFieldEvents) {
        when(e) {
            is ViewCropFieldEvents.GetRiceField -> initializeData(
                riceFieldId = e.riceFieldId
            )

            is ViewCropFieldEvents.OnStageSelected -> _state.value = _state.value.copy(
                selectedTab = e.stage
            )

            is ViewCropFieldEvents.OnDeleteCrop -> deleteCrop(e.id)
        }
    }

    private fun deleteCrop(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            riceFieldRepository.deleteCropField(id).onSuccess {
                _state.value = _state.value.copy(isLoading = false)
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it))
                _oneTimeEvents.send(OneTimeEvents.NavigateBack)
            }.onFailure {
                _state.value = _state.value.copy(isLoading = false)
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it.message.toString()))
            }
        }
    }

    private fun initializeData(riceFieldId: String) {
        val riceFieldFlow = riceFieldRepository.getRiceFieldWithId(riceFieldId)
        val task = taskRepository.getTasksByFieldId(riceFieldId)
        val reminders = taskRepository.getRemindersByFieldId(riceFieldId)
        combine(riceFieldFlow, task, reminders) { riceField, tasks, reminders ->
            Triple(riceField, tasks, reminders)
        }.onStart {
                _state.value = ViewCropFieldState(isLoading = true)
        }.onEach { (riceField, tasks, reminders) ->
            _state.value = ViewCropFieldState(
                isLoading = false,
                riceField = riceField,
                selectedTab = riceField.stage,
                tasks = tasks,
                reminders = reminders
            )
        }.launchIn(viewModelScope)
    }
}
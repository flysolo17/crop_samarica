package com.jmballangca.cropsamarica.presentation.create_crop_field

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Tasks
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.data.models.rice_field.getHarvestDate
import com.jmballangca.cropsamarica.data.models.rice_field.getRiceStage
import com.jmballangca.cropsamarica.data.models.task.Task
import com.jmballangca.cropsamarica.domain.repository.AyaRepository
import com.jmballangca.cropsamarica.domain.repository.TaskRepository
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.RiceVariety
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.SoilTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class CreateCropFieldViewModel @Inject constructor(
    private val ayaRepository: AyaRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {
    private var _state = MutableStateFlow(CreateCropFieldState())
    val state = _state.asStateFlow()

    private val _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()


    fun events(e : CreateCropFieldEvents) {
        when (e) {
            CreateCropFieldEvents.Submit -> {
                submit()
            }
            is CreateCropFieldEvents.OnCreateTask -> createTasks(e.tasks)
            is CreateCropFieldEvents.CropForm.Area -> {
                _state.value = _state.value.copy(areaSize = e.area)
            }
            is CreateCropFieldEvents.CropForm.Irrigation -> {
                _state.value = _state.value.copy(irrigationType = e.irrigation)
            }
            is CreateCropFieldEvents.CropForm.Location ->{
                _state.value = _state.value.copy(location = e.location)
            }
            is CreateCropFieldEvents.CropForm.Name -> {
                _state.value = _state.value.copy(name = e.name)
            }
            is CreateCropFieldEvents.CropForm.PlantedDate -> {
                _state.value = _state.value.copy(plantedDate = e.plantedDate)
            }
            is CreateCropFieldEvents.CropForm.SoilType -> {
                _state.value = _state.value.copy(soilType = e.soilType)
            }
            is CreateCropFieldEvents.CropForm.Variety -> {
                _state.value = _state.value.copy(variety = e.variety)
            }

            is CreateCropFieldEvents.CropForm.OnImageSelected -> {
                _state.value = _state.value.copy(imageUri = e.uri)
            }
        }
    }

    private fun createTasks(tasks: List<Task>) {
        _state.value = _state.value.copy(isCreatingTask = true)
        viewModelScope.launch {
            taskRepository.insertAll(tasks).onSuccess {
                _state.value = _state.value.copy(isCreatingTask = false)
                _oneTimeEvents.send(OneTimeEvents.NavigateBack)
            }.onFailure {
                _state.value = _state.value.copy(isCreatingTask = false)
            }
        }
    }

    private fun submit() {
        val data = state.value
        val stage = data.plantedDate?.getRiceStage() ?: RiceStage.SEEDLING
        val riceField = RiceField(
            name = data.name,
            location = data.location,
            plantedDate = data.plantedDate?.time ?: System.currentTimeMillis(),
            areaSize = data.areaSize.toDoubleOrNull() ?: 1.0,
            variety = data.variety ?: RiceVariety.NSIC_RC9,
            irrigationType = data.irrigationType,
            soilType = data.soilType ?: SoilTypes.CLAY,
            stage = stage,
            expectedHarvestDate = stage.getHarvestDate(
                data.variety ?: RiceVariety.NSIC_RC9
            ).time
        )
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            ayaRepository.generateRecommendation(
                data = riceField,
                imageUri = data.imageUri
            ).onSuccess {
                _state.value = _state.value.copy(
                    recommendationResult = it,
                    isLoading = false
                )
            }.onFailure {
                _state.value = _state.value.copy(
                    isLoading = false
                )
            }
        }

    }
}
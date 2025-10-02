package com.jmballangca.cropsamarica.presentation.weather_forecast

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.core.utils.Reminder
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.weather.SevenDayWeatherResponse
import com.jmballangca.cropsamarica.data.service.WeatherApiService
import com.jmballangca.cropsamarica.domain.repository.AyaRepository
import com.jmballangca.cropsamarica.domain.repository.SevenDayWeatherForecast
import com.jmballangca.cropsamarica.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WeatherForecastState(
    val isLoading: Boolean = false,
    val weatherForecast: SevenDayWeatherForecast? = null,
    val reminders: List<Reminder> = emptyList(),
    val isGeneratingReminder : Boolean = false,
    val aiReminders : List<Reminder> = emptyList(),
    val error: String? = null
)

sealed interface WeatherForecastEvents {
    data class LoadWeatherForecast(
        val id: String
    ) : WeatherForecastEvents

    data class OnGetReminders(
        val id: String
    ) : WeatherForecastEvents

    data class OnGenerateReminder(
        val weather: SevenDayWeatherResponse,
        val riceField: RiceField
    ) : WeatherForecastEvents
    data class OnNotify(
        val reminder: Reminder
    ) : WeatherForecastEvents
}
@HiltViewModel
class WeatherForecastViewModel @Inject constructor(
    private val ayaRepository: AyaRepository,
    private val taskRepository: TaskRepository,
) : ViewModel() {
    private var _state = MutableStateFlow(WeatherForecastState())
    val state = _state.asStateFlow()

    fun events(e: WeatherForecastEvents) {
        when(e) {
            is WeatherForecastEvents.LoadWeatherForecast -> {
                getWeatherForecast(e.id)
            }

            is WeatherForecastEvents.OnGenerateReminder -> generateReminder(e.weather, e.riceField)
            is WeatherForecastEvents.OnGetReminders -> getReminders(e.id)
            is WeatherForecastEvents.OnNotify -> notify(e.reminder)
        }
    }

    private fun notify(reminder: Reminder) {
        viewModelScope.launch {
            taskRepository.createReminders(reminder)
        }
    }

    private fun getWeatherForecast(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            ayaRepository.generateWeatherForecast(id).onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false,
                    weatherForecast = it
                )
            }.onFailure {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = it.message
                )
            }

        }
    }

    private fun getReminders(id: String) {
        taskRepository.getRemindersToday(id).onStart {
            _state.value = _state.value.copy(isGeneratingReminder = true)
        }.onEach {
            _state.value = _state.value.copy(
                isGeneratingReminder = false,
                reminders = it
            )
        }.catch {
            _state.value = _state.value.copy(
                isGeneratingReminder = false,
                error = it.message
            )
        }.launchIn(viewModelScope)
    }
    private fun generateReminder(
        weather: SevenDayWeatherResponse,
        riceField: RiceField
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isGeneratingReminder = true)
            ayaRepository.generateReminder(riceField, weather)
                .onSuccess {
                    _state.value = _state.value.copy(isGeneratingReminder = false,aiReminders = it)
                }
                .onFailure {
                    _state.value = _state.value.copy(isGeneratingReminder = false, error = it.message)
            }
        }
    }
}
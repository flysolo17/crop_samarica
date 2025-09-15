package com.jmballangca.cropsamarica

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.data.models.user.User
import com.jmballangca.cropsamarica.data.models.weather.BulkLocation
import com.jmballangca.cropsamarica.data.models.weather.LocationName
import com.jmballangca.cropsamarica.data.models.weather.WeatherApiResponse

import com.jmballangca.cropsamarica.data.service.WeatherApiService
import com.jmballangca.cropsamarica.domain.repository.AuthRepository
import com.jmballangca.cropsamarica.domain.repository.ForecastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


data class MainState(
    val isLoading: Boolean = false,
    val hasUser: Boolean = false,
    val user: User? = null
)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val forecastRepository: ForecastRepository,
    private val authRepository: AuthRepository,
    private val weatherApiService: WeatherApiService
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            _state.value = _state.value.copy(isLoading = true)
            authRepository.getUser().onSuccess {
                _state.value = _state.value.copy(hasUser = it != null, isLoading = false, user = it)
            }.onFailure {
                _state.value = _state.value.copy(hasUser = false, isLoading = false)

            }
        }
    }


    fun getWeather(location : String = "San Manuel Pangasinan") {
        viewModelScope.launch {
            val response = weatherApiService.getBulk(
                request = BulkLocation(
                    locations = listOf(
                        LocationName("Binalonan, Pangasinan"),
                        LocationName("San Manuel, Pangasinan")
                    )
                )
            )
            if (response.isSuccessful) {
                Log.d("MainViewModel", "getWeather: ${response.body()}")
            } else {
                Log.d("MainViewModel", "getWeather: ${response.errorBody()}")
            }
        }
    }

}
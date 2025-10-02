package com.jmballangca.cropsamarica.presentation.pest.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.data.models.pest.PestAndDisease
import com.jmballangca.cropsamarica.domain.repository.PestAndDiseasesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

data class PestAndDiseaseDetailState(
    val isLoading: Boolean = false,
    val pestAndDisease: PestAndDisease? = null
)
sealed interface PestAndDiseaseEvents {
    data class OnGetPestAndDisease(val id: String) : PestAndDiseaseEvents
}

@HiltViewModel
class PestAndDiseaseDetailViewModel @Inject constructor(
    private val pestAndDiseasesRepository: PestAndDiseasesRepository
) : ViewModel() {
    private var _state = MutableStateFlow(PestAndDiseaseDetailState())
    val state = _state.asStateFlow()
    fun events(e: PestAndDiseaseEvents) {
        when(e){
            is PestAndDiseaseEvents.OnGetPestAndDisease -> getPestAndDisease(e.id)
        }
    }

    private fun getPestAndDisease(id: String) {
        pestAndDiseasesRepository.getById(id).onStart {
            _state.value = _state.value.copy(isLoading = true)
            delay(1000)
        }.onEach {
            _state.value = _state.value.copy(isLoading = false, pestAndDisease = it)
        }.launchIn(viewModelScope)
    }
}
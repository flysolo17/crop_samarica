package com.jmballangca.cropsamarica.presentation.pest.pest_and_diseases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.data.models.pest.PestAndDisease
import com.jmballangca.cropsamarica.domain.repository.PestAndDiseasesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PestAndDiseaseState(
    val isLoading : Boolean = false,
    val pestAndDiseases : List<PestAndDisease> = emptyList()
)
@HiltViewModel
class PestAndDiseasesViewModel @Inject constructor(
    private val pestAndDiseasesRepository: PestAndDiseasesRepository
): ViewModel() {
    private val _state = MutableStateFlow(PestAndDiseaseState())
    val state = _state.asStateFlow()
    init {
        getAll()
    }

    private fun getAll() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val pestAndDiseases = pestAndDiseasesRepository.getAll()
            delay(1000)
            _state.value = _state.value.copy(
                isLoading = false,
                pestAndDiseases = pestAndDiseases
            )
        }
    }

}
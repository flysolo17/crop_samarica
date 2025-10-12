package com.jmballangca.cropsamarica.presentation.pest.pest_and_diseases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.core.common.LocaleManager
import com.jmballangca.cropsamarica.data.models.pest.PestAndDisease
import com.jmballangca.cropsamarica.domain.repository.PestAndDiseasesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PestAndDiseaseState(
    val isLoading : Boolean = false,
    val pestAndDiseases : List<PestAndDisease> = emptyList(),
    val language : String = "en"
)
sealed interface PestAndDiseaseEvents {
    data class OnLanguageChanged(val language: String) : PestAndDiseaseEvents
}
@HiltViewModel
class PestAndDiseasesViewModel @Inject constructor(
    private val pestAndDiseasesRepository: PestAndDiseasesRepository,
    private val localeManager: LocaleManager
): ViewModel() {
    private val _state = MutableStateFlow(PestAndDiseaseState())
    val state = _state.asStateFlow()
    init {
        getAll()
        localeManager.getSavedLanguageCode().onEach {
            _state.value = _state.value.copy(language = it)
        }.launchIn(viewModelScope)
    }
    fun events(
        e : PestAndDiseaseEvents
    ) {
        when(e) {
            is PestAndDiseaseEvents.OnLanguageChanged -> {
                //refresh the context so that xml files are updated
                localeManager.updateLocale(e.language)
            }
        }
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
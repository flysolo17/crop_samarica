package com.jmballangca.cropsamarica.presentation.developers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.domain.models.Developers
import com.jmballangca.cropsamarica.domain.repository.CommonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class DeveloperState(
    val developers: List<Developers> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DeveloperViewModel @Inject constructor(
    private val repository: CommonRepository
)  : ViewModel(){
    private val _state = MutableStateFlow(DeveloperState())
    val state = _state.asStateFlow()
    init {
        getAll()
    }

    fun getAll() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val developers = repository.getAllDevelopers()
                _state.update { it.copy(developers = developers, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}
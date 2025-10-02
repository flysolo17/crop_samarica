package com.jmballangca.cropsamarica.presentation.user_guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.domain.models.UserGuide
import com.jmballangca.cropsamarica.domain.repository.CommonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class UserGuideState(
    val userGuides: List<UserGuide> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class UserGuideViewModel @Inject constructor(
    private val repository : CommonRepository
) : ViewModel() {
    private val _state = MutableStateFlow(UserGuideState())
    val state = _state.asStateFlow()
    init {
        getAll()
    }
    fun getAll(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val userGuides = repository.getAllUserGuides()
                _state.update { it.copy(userGuides = userGuides, isLoading = false) }
                } catch (e: Exception) {
                _state.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}
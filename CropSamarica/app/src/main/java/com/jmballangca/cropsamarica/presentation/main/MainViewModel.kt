package com.jmballangca.cropsamarica.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.domain.repository.AuthRepository
import com.jmballangca.cropsamarica.domain.repository.RiceFieldRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val riceFieldRepository: RiceFieldRepository
): ViewModel() {
    private var _mainState = MutableStateFlow(
        MainState()
    )
    val mainState = _mainState.asStateFlow()
    init {
        getUserFlow()
    }

    fun events(e : MainEvents) {
        when(e) {
            is MainEvents.SelectRiceField -> {
                _mainState.value = _mainState.value.copy(selectedRiceField = e.riceField)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getUserFlow() {
        authRepository.getUserRealTime()
            .onStart {
                _mainState.value = _mainState.value.copy(isLoading = true)
            }
            .flatMapLatest { user ->
                _mainState.value = _mainState.value.copy(user = user)
                val riceFieldFlow = user?.let {
                    riceFieldRepository.getAllByUid(it.id)
                } ?: flowOf(emptyList())

                riceFieldFlow.map { riceFields ->
                    user to riceFields
                }
            }
            .onEach { (_, riceFields) ->
                _mainState.value = _mainState.value.copy(
                    isLoading = false,
                    riceFields = riceFields,
                    selectedRiceField = riceFields.getOrNull(0)
                )
            }
            .launchIn(viewModelScope)
    }
}
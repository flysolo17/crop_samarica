package com.jmballangca.cropsamarica.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents.*
import com.jmballangca.cropsamarica.core.utils.UIState
import com.jmballangca.cropsamarica.domain.repository.AuthRepository
import com.jmballangca.cropsamarica.presentation.navigation.AUTH
import com.jmballangca.cropsamarica.presentation.navigation.ONBOARDING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private var _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()
    private var _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()
    init {
        authRepository.getUserRealTime().onStart {
            _state.value = _state.value.copy(isLoading = true)
        }.onEach {
            _state.value = _state.value.copy(isLoading = false, user = it)
        }.launchIn(viewModelScope)
    }

    fun events(e : ProfileEvents) {
        when(e) {
            ProfileEvents.OnLogout -> {
                viewModelScope.launch {
                    authRepository.logout().onSuccess {
                        _oneTimeEvents.trySend(ShowToast("Logout Successful"))
                    }
                }
            }

            is ProfileEvents.OnChangeName -> changeName(e.name,e.result)
            is ProfileEvents.OnChangePassword -> changePassword(
                e.currentPassword,
                e.newPassword,
                e.result
            )
        }
    }

    private fun changePassword(
        currentPassword: String,
        newPassword: String,
        result: (UIState<String>) -> Unit
    ): Job {
        return viewModelScope.launch {
            authRepository.changePassword(currentPassword, newPassword) {
                result.invoke(it)
            }
        }
    }

    private fun changeName(
        name: String,
        result: (UIState<String>) -> Unit
    ): Job {
        return viewModelScope.launch {
            authRepository.changeName(state.value.user?.id ?: "", name) {
                result.invoke(it)
            }
        }
    }
}
package com.jmballangca.cropsamarica.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.domain.repository.AuthRepository
import com.jmballangca.cropsamarica.presentation.navigation.MAIN
import com.jmballangca.cropsamarica.presentation.navigation.VERIFICATION
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private var _state = MutableStateFlow(
        AuthState()
    )
    val state = _state.asStateFlow()
    private var _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()
    fun events(e : AuthEvents) {
        when (e) {
            is AuthEvents.Login -> {
                login(e.email, e.password)
            }
            is AuthEvents.Register -> {
                register(e.name, e.email, e.password)
            }

            is AuthEvents.ForgotPassword -> forgotPassword(e.email)
            AuthEvents.RegisterWithGoogle -> registerWithGoogle()
            AuthEvents.SignWithGoogle -> signInWithGoogle()
        }
    }

    private fun registerWithGoogle() {
        viewModelScope.launch {
            _state.value = state.value.copy(
                isRegistering = true
            )
            authRepository.registerWithGoogle().onSuccess {
                _state.value = state.value.copy(
                    isRegistering = false,
                )
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it))
                _oneTimeEvents.send(OneTimeEvents.Navigate(VERIFICATION))
            }.onFailure {
                _state.value = state.value.copy(
                    isRegistering = false,
                )
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it.message.orEmpty()))
            }
        }
    }

    private fun signInWithGoogle() {
        viewModelScope.launch {
            _state.value = state.value.copy(
                isLoading = true
            )
            authRepository.signInWithGoogle().onSuccess {
                _state.value = state.value.copy(
                    isLoading = false,
                )
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it))
                _oneTimeEvents.send(OneTimeEvents.Navigate(MAIN))
            }.onFailure {
                _state.value = state.value.copy(isLoading = false)
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it.message.orEmpty()))
            }
        }
    }
    private fun forgotPassword(email: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                isSubmitting = true
            )
            authRepository.forgotPassword(email).onSuccess {
                _state.value = state.value.copy(
                    isSubmitting = false,
                )
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it))
            }.onFailure {
                _state.value = state.value.copy(
                    isSubmitting = false,
                )
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it.message.orEmpty()))
            }
        }
    }

    private fun register(name: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = state.value.copy(
                isRegistering = true
            )
            authRepository.register(name, email, password).onSuccess {
                _state.value = state.value.copy(
                    isRegistering = false,
                )
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it))
                _oneTimeEvents.send(OneTimeEvents.Navigate(VERIFICATION))
            }.onFailure {
                _state.value = state.value.copy(
                    isRegistering = false,
                )
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it.message.orEmpty()))
            }
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                isLoading = true
            )
            authRepository.login(email, password).onSuccess {
                _state.value = state.value.copy(
                    isLoading = false,
                )
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it))
                _oneTimeEvents.send(OneTimeEvents.Navigate(MAIN))
                }.onFailure {
                    _state.value = state.value.copy(isLoading = false)
                _oneTimeEvents.send(OneTimeEvents.ShowToast(it.message.orEmpty()))
            }
        }
    }
}
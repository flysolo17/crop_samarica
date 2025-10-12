package com.jmballangca.cropsamarica.presentation.auth.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.domain.repository.AuthRepository
import com.jmballangca.cropsamarica.presentation.navigation.MAIN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class VerificationState(
    val isLoading : Boolean = false,
    val timer : Long = 0L,
    val isVerified : Boolean = false
)

sealed interface VerificationEvents {
    object SendEmailVerification : VerificationEvents
}

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(VerificationState())
    val state = _state.asStateFlow()

    private var timerJob: Job? = null
    private var _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()
    init {
        listenToUserVerification()
    }

    fun events(e: VerificationEvents) {
        when (e) {
            VerificationEvents.SendEmailVerification -> {
                sendEmailVerification()
            }
        }
    }

    private fun sendEmailVerification() {
        viewModelScope.launch {
            authRepository.sendEmailVerification().onSuccess {
                _oneTimeEvents.send(
                    OneTimeEvents.ShowToast("We have sent you an email verification")
                )
                startTimer()
            }.onFailure {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun startTimer() {

        timerJob?.cancel()

        // Start 60-second countdown
        timerJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val totalTime = 60L // seconds
            for (i in totalTime downTo 0) {
                _state.update { it.copy(timer = i) }
                delay(1000L)
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    private fun listenToUserVerification() {
        viewModelScope.launch {
            while (true) {
                delay(3000) // Poll every 3 seconds
                val result = authRepository.checkUserVerified()
                if (result.isSuccess) {
                    val isVerified = result.getOrNull() ?: false
                    if (isVerified) {
                        _oneTimeEvents.send(
                            OneTimeEvents.Navigate(MAIN)
                        )
                        break
                    }
                } else {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Error checking email verification."
                    _oneTimeEvents.send(
                        OneTimeEvents.ShowToast(errorMessage)
                    )
                }
            }
        }
    }

}
package com.jmballangca.cropsamarica.presentation.notifications

import android.app.Notification
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.domain.models.NotificationType
import com.jmballangca.cropsamarica.domain.models.Notifications
import com.jmballangca.cropsamarica.domain.repository.CommonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

data class NotificationState(
    val isLoading : Boolean = false,
    val notifications : List<Notifications> = emptyList(),
    val selectedType : NotificationType = NotificationType.all
)

sealed interface NotificationEvents {

    data class SelectType(
        val type : NotificationType
    ) : NotificationEvents
}

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val commonRepository: CommonRepository
): ViewModel() {
    private val _state = MutableStateFlow(NotificationState())
    val state = _state.asStateFlow()

    fun events(event: NotificationEvents) {
        when(event) {


            is NotificationEvents.SelectType -> selectType(event.type)
        }
    }
    private fun selectType(type: NotificationType) {
        _state.value = _state.value.copy(selectedType = type)
    }
//
//    private fun loadNotifications(uid: String) {
//        commonRepository.getAllMyNotifications(uid).onStart {
//            _state.value = _state.value.copy(isLoading = true)
//        }.onEach {
//            _state.value = _state.value.copy(isLoading = false, notifications = it)
//        }.launchIn(viewModelScope)
//    }
}
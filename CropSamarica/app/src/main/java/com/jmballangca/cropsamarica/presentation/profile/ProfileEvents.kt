package com.jmballangca.cropsamarica.presentation.profile

import com.jmballangca.cropsamarica.core.utils.UIState


sealed interface ProfileEvents{
    object OnLogout : ProfileEvents
    data class OnChangeName(val name : String,val result : (UIState<String>) -> Unit) : ProfileEvents
    data class OnChangePassword(val currentPassword : String,val newPassword : String,val result : (UIState<String>) -> Unit) : ProfileEvents
}
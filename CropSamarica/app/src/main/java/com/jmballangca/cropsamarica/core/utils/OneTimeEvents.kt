package com.jmballangca.cropsamarica.core.utils


sealed interface OneTimeEvents {
    data class ShowToast(val message: String) : OneTimeEvents
    data class Navigate(val route: Any) : OneTimeEvents

    object NavigateBack : OneTimeEvents
}
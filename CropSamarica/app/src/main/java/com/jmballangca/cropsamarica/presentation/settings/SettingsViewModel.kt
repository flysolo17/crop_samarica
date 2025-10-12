package com.jmballangca.cropsamarica.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.core.common.LocaleManager
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SettingsState(
    val isLoading : Boolean = false,
    val language : String = "en"
)

sealed interface SettingsEvents {
    data class OnLanguageChanged(val language : String) : SettingsEvents
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val localeManager: LocaleManager
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()
    init {
        localeManager.getSavedLanguageCode().onEach {
            _state.value = _state.value.copy(
                language = it
            )
        }.launchIn(viewModelScope)
    }
    private var _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()
    fun events(events: SettingsEvents) {
        when(events) {
            is SettingsEvents.OnLanguageChanged -> changeLanguage(events.language)
        }
    }

    private fun changeLanguage(language: String) {
        viewModelScope.launch {
            localeManager.updateLocale(language)

            _oneTimeEvents.send(OneTimeEvents.ShowToast("change"))
        }

    }

}
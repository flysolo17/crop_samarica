package com.jmballangca.cropsamarica.presentation.chat


sealed interface ChatEvents {

    data class SendMessage(val message: String) : ChatEvents
}
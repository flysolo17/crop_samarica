package com.jmballangca.cropsamarica.presentation.chat

import com.jmballangca.cropsamarica.data.local.models.chat.Conversation
import com.jmballangca.cropsamarica.data.local.models.chat.ConversationWithMessages
import com.jmballangca.cropsamarica.data.local.models.chat.Message
import com.jmballangca.cropsamarica.data.models.user.User


data class ChatState(
    val isLoading : Boolean = false,
    val conversations : List<Conversation> = emptyList(),
    val messages : List<Message> = emptyList(),
    val user: User? = null,
    val selectedConversation: Conversation? = null
)
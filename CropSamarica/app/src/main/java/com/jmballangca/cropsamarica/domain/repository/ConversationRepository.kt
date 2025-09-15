package com.jmballangca.cropsamarica.domain.repository

import com.jmballangca.cropsamarica.data.local.models.chat.Conversation
import com.jmballangca.cropsamarica.data.local.models.chat.Message
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    suspend fun insertNew(
        uid: String,
        message: Message,
    ) : Result<Conversation>
    fun getConversations(
        uid : String
    ) : Flow<List<Conversation>>


    fun getMessages(
        conversationId: Long
    ) : Flow<List<Message>>

}
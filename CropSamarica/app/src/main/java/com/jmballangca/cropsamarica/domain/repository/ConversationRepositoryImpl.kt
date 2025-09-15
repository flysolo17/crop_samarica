package com.jmballangca.cropsamarica.domain.repository

import com.jmballangca.cropsamarica.data.local.dao.ConversationDao
import com.jmballangca.cropsamarica.data.local.dao.MessagesDao
import com.jmballangca.cropsamarica.data.local.models.chat.Conversation
import com.jmballangca.cropsamarica.data.local.models.chat.Message
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val conversationDao: ConversationDao,
    private val messagesDao: MessagesDao
): ConversationRepository {
    override suspend fun insertNew(
        uid: String,
        message: Message
    ): Result<Conversation> {
        return try {
            val conversation = Conversation(
                title = message.text,
                uid = uid,
            )
            val id = conversationDao.insert(conversation)
            message.conversationId = id
            messagesDao.insertAll(message)
            Result.success(conversation)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getConversations(uid: String): Flow<List<Conversation>> {
       return conversationDao.getConversations(uid)
    }

    override fun getMessages(conversationId: Long): Flow<List<Message>> {
        return messagesDao.getMessages(conversationId)
    }
}
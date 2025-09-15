package com.jmballangca.cropsamarica.data.local.models.chat

import androidx.room.Embedded
import androidx.room.Relation

data class ConversationWithMessages(
    @Embedded val conversation: Conversation,
    @Relation(
        parentColumn = "id",
        entityColumn = "conversation_id"
    )
    val messages : List<Message>
)
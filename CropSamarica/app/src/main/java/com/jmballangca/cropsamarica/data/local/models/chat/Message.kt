package com.jmballangca.cropsamarica.data.local.models.chat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(
        autoGenerate = true
    )
    val id : Long = 0,
    @ColumnInfo(
        name = "conversation_id"
    )
    var conversationId : Long,
    val user : MessageUser,
    val text : String,
    @ColumnInfo(name = "timestamp")
    val timestamp : Long = System.currentTimeMillis()
)
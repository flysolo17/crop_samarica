package com.jmballangca.cropsamarica.data.local.models.chat

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(tableName = "conversations")
data class Conversation(
    @PrimaryKey(
        autoGenerate = true
    )
    val id : Long = 0,
    val uid : String,
    val title: String,
    @ColumnInfo(name = "created_at",)
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)



enum class MessageUser {
    USER,
    BOT
}
package com.jmballangca.cropsamarica.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jmballangca.cropsamarica.data.local.models.chat.Message
import kotlinx.coroutines.flow.Flow


@Dao
interface MessagesDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: Message): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        vararg messages: Message
    )

    @Delete
    suspend fun delete(
        message: Message
    )

    @Query("SELECT * FROM messages WHERE conversation_id = :conversationId")
    fun getMessages(
        conversationId : Long
    ) : Flow<List<Message>>
}
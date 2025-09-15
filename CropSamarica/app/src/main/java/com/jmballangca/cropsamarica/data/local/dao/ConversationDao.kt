package com.jmballangca.cropsamarica.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jmballangca.cropsamarica.data.local.models.chat.Conversation
import com.jmballangca.cropsamarica.data.local.models.chat.ConversationWithMessages
import com.jmballangca.cropsamarica.data.local.models.chat.Message
import kotlinx.coroutines.flow.Flow


@Dao
interface ConversationDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(conversation: Conversation): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        vararg conversations: Conversation
    )

    @Delete
    suspend fun delete(
        conversation: Conversation
    )


    @Query("SELECT * FROM conversations WHERE uid = :uid")
    fun getConversations(
        uid : String
    ) : Flow<List<Conversation>>

    @Transaction
    @Query("SELECT * FROM conversations WHERE id = :id")
    fun getConversationWithMessages(
        id : Long
    ) : Flow<ConversationWithMessages>

}
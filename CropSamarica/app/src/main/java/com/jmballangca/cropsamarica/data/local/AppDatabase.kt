package com.jmballangca.cropsamarica.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jmballangca.cropsamarica.data.local.dao.ConversationDao
import com.jmballangca.cropsamarica.data.local.dao.MessagesDao
import com.jmballangca.cropsamarica.data.local.models.chat.Conversation
import com.jmballangca.cropsamarica.data.local.models.chat.Message
import com.jmballangca.cropsamarica.data.local.models.chat.MessageUserConverter

@Database(
    entities = [
        Conversation::class,
        Message::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(MessageUserConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun conversationDao(): ConversationDao
    abstract fun messagesDao(): MessagesDao

    companion object {
        const val DATABASE_NAME = "crop_samarica_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration(false).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

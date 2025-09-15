package com.jmballangca.cropsamarica.data.local.models.chat

import androidx.room.TypeConverter

class MessageUserConverter {
    @TypeConverter
    fun fromMessageUser(value: MessageUser): String = value.name

    @TypeConverter
    fun toMessageUser(value: String): MessageUser = MessageUser.valueOf(value)
}

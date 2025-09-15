package com.jmballangca.cropsamarica.data.models.rice_field

import com.jmballangca.cropsamarica.domain.utils.toDateOnly
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import java.util.Date


data class Announcement(
    val id : String = "",
    val title : String = "",
    val message : String = "",
    val fieldId : String = "",
    val date : String = "",
    val urgency : String = "",
    val createdAt : Long = System.currentTimeMillis(),
    val updatedAt : Long = System.currentTimeMillis()
)

fun Map<String, JsonElement>.asAnnouncement(
    fieldId: String
): Announcement {
    val now = System.currentTimeMillis()

    return Announcement(
        id = "", // Firestore will assign when saving
        title = this["title"]?.jsonPrimitive?.contentOrNull ?: "No title",
        message = this["message"]?.jsonPrimitive?.contentOrNull ?: "No message",
        fieldId = fieldId,
        date = Date().toDateOnly(),
        urgency = this["urgency"]?.jsonPrimitive?.contentOrNull ?: "LOW",
        createdAt = now,
        updatedAt = now
    )
}

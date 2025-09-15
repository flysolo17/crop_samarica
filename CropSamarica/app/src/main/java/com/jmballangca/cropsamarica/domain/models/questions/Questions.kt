package com.jmballangca.cropsamarica.domain.models.questions

import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive



data class QuestionsWithCurrentStage(
    val questions: List<Question>,
    val riceField: RiceField
)


@Serializable
data class Question(
    val text: String ? = null,
    val type: QuestionType ? = null,
    val options: List<String>? = null
)

@Serializable
enum class QuestionType {
    MULTIPLE_CHOICE,
    SINGLE_CHOICE,
    SHORT_ANSWER,
    LONG_ANSWER,
    TRUE_FALSE
}


fun Map<String, JsonElement>.asQuestion(): Question? {
    val text = this["text"]?.jsonPrimitive?.content ?: return null
    val typeStr = this["type"]?.jsonPrimitive?.content ?: return null
    val type = QuestionType.valueOf(typeStr.uppercase())
    val options = this["options"]?.jsonArray?.mapNotNull { it.jsonPrimitive.content }
    return Question(text, type, options)
}

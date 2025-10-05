package com.jmballangca.cropsamarica.core.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.ai.type.FunctionDeclaration
import com.google.firebase.ai.type.Schema
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.presentation.weather_forecast.toFormattedDate
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Reminder(
    var id : String = "",
    var uid : String = "",
    val riceFieldId : String = "",
    val message : String = "",
    val bestApplicationTime :List<BestApplicationTime> = emptyList(),
    val reminderDate : Date = Date(),
    val stage : RiceStage = RiceStage.SEEDLING,
    val createdAt : Date = Date(),
)
data class BestApplicationTime(
    val time : String = "",
    val condition : ApplicationCondition = ApplicationCondition.OPTIMAL
)

enum class ApplicationCondition(
    val displayName : String ,
    val image : ImageVector,
    val color : androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified
) {
    OPTIMAL(
       displayName =  "Optimal",
        image = Icons.Filled.Check,
        color = androidx.compose.ui.graphics.Color.Green
    ),
    UNFAVORABLE(
       displayName =  "Unfavorable",
       image= Icons.Filled.Close,
        color = androidx.compose.ui.graphics.Color.Red
    ),

    MODERATE(
        displayName = "Moderate",
        image = Icons.Filled.Warning,
        color = androidx.compose.ui.graphics.Color.Yellow
    )
}


const val REMINDER = "reminder"

val CREATE_REMINDER = FunctionDeclaration(
    name = REMINDER,
    description = "Generates a list of reminders for the farmer to do based on the rice field data and weather forecast. The list will contain at least 2 distinct reminders.",
    parameters = mapOf(
        "reminders" to Schema.array(
            Schema.obj(
                mapOf(
                    "id" to Schema.string("A unique identifier for this reminder."),
                    "riceFieldId" to Schema.string("The ID of the specific rice field this reminder applies to."),
                    "stage" to Schema.string("The stage of the rice field at the time of the reminder."),
                    "message" to Schema.string("A short, clear, practical, and concise, farmer-friendly message detailing the reminder."),
                    "bestApplicationTime" to Schema.array(
                        // *** IMPROVED SCHEMA FOR bestApplicationTime ***
                        description = "An hourly assessment of application suitability from 6 AM to 9 PM (21:00) for the reminderDate, based on the weather forecast. Each entry represents a specific hour within this window, indicating its suitability ('OPTIMAL', 'UNFAVORABLE', 'MODERATE'). There must be exactly one entry for each hour from 6 AM to 9 PM (16 entries total).",
                        items = Schema.obj(
                            mapOf(
                                "time" to Schema.string("The specific hour of the day, e.g., '6 AM', '7 AM', ..., '9 PM' (for 21:00)."),
                                "condition" to Schema.string("The condition indicating suitability for application at this hour. Must be one of 'OPTIMAL', 'UNFAVORABLE', or 'MODERATE'.")
                            )
                        ),
                        minItems = 16,
                        maxItems = 16
                    ),
                    "reminderDate" to Schema.string(
                        "The date of the reminder. Must always be between today and the next 6 days inclusive, based on the weather forecast. Format must be 'yyyy-MM-dd'."
                    )
                )
            )
        )
    ),
    optionalParameters = emptyList()
)

fun JsonObject.asReminder(): Reminder {
    // Define the date format expected from the AI.
    // Using Locale.US is generally safer for machine-generated strings to avoid locale issues.
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en","PH")) // Changed Locale.CHINA to Locale.US for consistency
    val id = this["id"]?.jsonPrimitive?.contentOrNull
        ?: throw IllegalArgumentException("Reminder 'id' is missing or not a valid string.")
    val riceFieldId = this["riceFieldId"]?.jsonPrimitive?.contentOrNull
        ?: throw IllegalArgumentException("Reminder 'riceFieldId' is missing or not a valid string.")
    val message = this["message"]?.jsonPrimitive?.contentOrNull
        ?: throw IllegalArgumentException("Reminder 'message' is missing or not a valid string.")

    // Process the 'bestApplicationTime' array
    val bestApplicationTime = this["bestApplicationTime"]?.jsonArray?.map { element ->
        // Each element in the array should be a JsonObject
        val timeObj = element.jsonObject
        val time = timeObj["time"]?.jsonPrimitive?.contentOrNull
            ?: throw IllegalArgumentException("BestApplicationTime 'time' is missing or not a valid string.")
        val conditionString = timeObj["condition"]?.jsonPrimitive?.contentOrNull
            ?: throw IllegalArgumentException("BestApplicationTime 'condition' is missing or not a valid string.")

        // Convert the string to the ApplicationCondition enum
        val condition = try {
            ApplicationCondition.valueOf(conditionString)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Invalid ApplicationCondition value: '$conditionString'", e)
        }
        BestApplicationTime(time = time, condition = condition)
    } ?: emptyList() // If 'bestApplicationTime' is missing or malformed, default to an empty list

    // Extract and parse the reminderDate
    val reminderDateString = this["reminderDate"]?.jsonPrimitive?.contentOrNull
        ?: throw IllegalArgumentException("Reminder 'reminderDate' is missing or not a valid string.")

    val reminderDate = dateFormat.parse(reminderDateString)

    return Reminder(
        id = id,
        riceFieldId = riceFieldId,
        message = message,
        bestApplicationTime = bestApplicationTime,
        reminderDate = reminderDate
    )
}
package com.jmballangca.cropsamarica.data.models.task

import androidx.compose.ui.graphics.Color
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.domain.models.Recommendation
import java.util.Date


data class Task(
    var id: String = "",
    val fieldId: String = "",
    val title: String = "",
    val description: String = "",
    val stage : RiceStage = RiceStage.SEEDLING,
    val status: TaskStatus = TaskStatus.PENDING,
    val createdAt : Date = Date(),
    val updatedAt : Date = Date(),
)
fun TaskStatus.getBackgroundColor(): Color {
    return when (this) {
        TaskStatus.PENDING -> Color(0xFFFFCDD2)   // Muted Red (light red)
        TaskStatus.IN_PROGRESS -> Color(0xFFFFF9C4) // Muted Yellow (light yellow)
        TaskStatus.COMPLETED -> Color(0xFFC8E6C9) // Muted Green (light green)
    }
}

fun TaskStatus.getTextColor(): Color {
    return when (this) {
        TaskStatus.PENDING -> Color(0xFFB71C1C)   // Dark Red
        TaskStatus.IN_PROGRESS -> Color(0xFFF57F17) // Dark Yellow/Amber
        TaskStatus.COMPLETED -> Color(0xFF1B5E20) // Dark Green
    }
}
fun Recommendation.toTask(
    riceFieldId: String
): Task {
    return Task(
        id = this.id,
        title = this.title,
        fieldId = riceFieldId,
        description = this.details,
        status = TaskStatus.PENDING,
        createdAt = Date(),
        updatedAt = Date(),
    )
}

enum class TaskStatus(
    val title: String
) {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed")
}
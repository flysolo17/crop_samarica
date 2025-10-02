package com.jmballangca.cropsamarica.data.models.task

import androidx.compose.ui.graphics.Color
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.domain.models.Recommendation
import java.util.Date


data class RiceFieldTask(
    val name : String = "",
    val stage : RiceStage = RiceStage.SEEDLING,
    val tasks : List<Task> = emptyList()
)
data class Task(
    var id: String = "",
    var uid : String = "",
    val fieldId: String = "",
    val title: String = "",
    val description: String = "",
    val stage : RiceStage = RiceStage.SEEDLING,
    val status: TaskStatus = TaskStatus.PENDING,
    val startDate : Date ? = null,
    val dueDate : Date ?= null,
    val createdAt : Date = Date(),
    val updatedAt : Date = Date(),
)


fun Task.isStartingToday() : Boolean {
    val today = Date()
    val taskDate = this.startDate
    return today.day == taskDate?.day && today.month == taskDate.month && today.year == taskDate.year
}

fun Date.isDueToday() : Boolean {
    val today = Date()
    val dueDate = this
    return today.day == dueDate.day && today.month == dueDate.month && today.year == dueDate.year
}

fun TaskStatus.getBackgroundColor(): Color {
    return when (this) {
        TaskStatus.PENDING -> Color(0xFFFFCDD2)
        TaskStatus.IN_PROGRESS -> Color(0xFFFFF9C4)
        TaskStatus.COMPLETED -> Color(0xFFC8E6C9)
    }
}

fun TaskStatus.getTextColor(): Color {
    return when (this) {
        TaskStatus.PENDING -> Color(0xFFB71C1C)   // Dark Red
        TaskStatus.IN_PROGRESS -> Color(0xFFF57F17) // Dark Yellow/Amber
        TaskStatus.COMPLETED -> Color(0xFF1B5E20) // Dark Green
    }
}
fun Task.getBackgroundColor(): Color {
    val now = Date()

    return when {
        // ✅ Completed task (always green background)
        status == TaskStatus.COMPLETED -> Color(0xFFC8E6C9)

        // Overdue task (past due date, not completed)
        dueDate != null && dueDate.before(now) -> Color(0xFFFFCDD2)

        // Task starts today (and has no due date)
        startDate != null && dueDate == null && startDate.isDueToday() -> Color(0xFFFFF9C4)

        // Upcoming task (start date is in the future)
        startDate != null && startDate.after(now) -> Color(0xFFE3F2FD)

        // Fallback: status-based color
        else -> status.getBackgroundColor()
    }
}

fun Task.getTextColor(): Color {
    val now = Date()

    return when {
        // ✅ Completed task (always green text)
        status == TaskStatus.COMPLETED -> Color(0xFF388E3C)

        // Overdue task (past due date, not completed)
        dueDate != null && dueDate.before(now) -> Color(0xFFB71C1C)

        // Task starts today (no due date)
        startDate != null && dueDate == null && startDate.isDueToday() -> Color(0xFFF57F17)

        // Upcoming task (start date is in the future)
        startDate != null && startDate.after(now) -> Color(0xFF1976D2)

        // Fallback: status-based color
        else -> status.getTextColor()
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


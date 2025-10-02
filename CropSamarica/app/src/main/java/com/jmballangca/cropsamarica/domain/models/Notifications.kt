package com.jmballangca.cropsamarica.domain.models

import java.util.Date



enum class NotificationType(
    val displayName: String
) {
    all("All"),
    rice_field("Rice Field"),
    task("Tasks"),
    reminders("Reminders"),
    announcements("Announcements")
}

enum class NotificationStatus(val value: String) {
    seen("seen"),
    unseen("unseen")
}

data class Notifications(
    val id: String = "",
    val uid: String = "",
    val action: String = "",
    val type: NotificationType = NotificationType.rice_field,
    val title: String = "",
    val message: String = "",
    val schedule: Date? = null,
    val status: String = NotificationStatus.unseen.value,
    val riceFieldId: String? = null,
    val sent: Boolean = false,
    val timestamp: Date? = null
)


val SAMPLE_NOTIFICATIONS = listOf(
    Notifications(
        id = "N001",
        uid = "user123",
        action = "created",
        type = NotificationType.rice_field,
        title = "Rice Field Added",
        message = "Your new rice field 'North Field' was created successfully.",
        riceFieldId = "RF001",
        sent = true,
        timestamp = Date()
    ),
    Notifications(
        id = "N002",
        uid = "user123",
        action = "updated",
        type = NotificationType.rice_field,
        title = "Rice Field Updated",
        message = "Your rice field 'North Field' details were updated.",
        riceFieldId = "RF001",
        sent = true,
        timestamp = Date()
    ),
    Notifications(
        id = "N003",
        uid = "user123",
        action = "deleted",
        type = NotificationType.rice_field,
        title = "Rice Field Deleted",
        message = "Your rice field 'Old Field' was deleted.",
        riceFieldId = "RF002",
        sent = true,
        timestamp = Date()
    ),
    Notifications(
        id = "N004",
        uid = "user456",
        action = "created",
        type = NotificationType.task,
        title = "New Task Assigned",
        message = "You have a new task: Apply fertilizer to 'South Field'.",
        riceFieldId = "RF003",
        sent = false,
        schedule = Date(), // maybe scheduled for today
        timestamp = Date()
    ),
    Notifications(
        id = "N005",
        uid = "user456",
        action = "updated",
        type = NotificationType.task,
        title = "Task Updated",
        message = "The task 'Irrigate North Field' has been updated.",
        riceFieldId = "RF001",
        sent = false,
        schedule = Date(),
        timestamp = Date()
    ),
    Notifications(
        id = "N006",
        uid = "user789",
        action = "created",
        type = NotificationType.reminders,
        title = "Reminder Set",
        message = "Reminder: Check pests in 'East Field'.",
        riceFieldId = "RF004",
        schedule = Date(),
        sent = false,
        timestamp = Date()
    ),
    Notifications(
        id = "N007",
        uid = "user789",
        action = "created",
        type = NotificationType.reminders,
        title = "Reminder Due",
        message = "Reminder today: Harvest 'West Field'.",
        riceFieldId = "RF005",
        schedule = Date(),
        sent = true,
        timestamp = Date()
    ),
    Notifications(
        id = "N008",
        uid = "user321",
        action = "created",
        type = NotificationType.announcements,
        title = "System Update",
        message = "New feature: Weather forecast integrated into the app.",
        sent = true,
        timestamp = Date()
    ),
    Notifications(
        id = "N009",
        uid = "user321",
        action = "created",
        type = NotificationType.announcements,
        title = "Maintenance Notice",
        message = "The app will be under maintenance tomorrow from 1AM–3AM.",
        sent = true,
        timestamp = Date()
    ),
    Notifications(
        id = "N010",
        uid = "user999",
        action = "created",
        type = NotificationType.task,
        title = "Task Overdue",
        message = "The task 'Weed control in Central Field' is overdue.",
        riceFieldId = "RF006",
        sent = true,
        timestamp = Date(),
        status = NotificationStatus.unseen.value
    )
)

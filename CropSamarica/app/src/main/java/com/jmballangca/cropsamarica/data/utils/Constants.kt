package com.jmballangca.cropsamarica.data.utils

import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStatus
import java.util.Date
import java.util.concurrent.TimeUnit


fun Date.asNotificationDate(): String {
    val now = Date()
    val diffMillis = now.time - this.time

    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
    val hours = TimeUnit.MILLISECONDS.toHours(diffMillis)
    val days = TimeUnit.MILLISECONDS.toDays(diffMillis)
    val weeks = days / 7
    val months = days / 30
    val years = days / 365

    return when {
        minutes < 1 -> "just now"
        minutes < 60 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
        hours < 24 -> "$hours hour${if (hours > 1) "s" else ""} ago"
        days < 7 -> "$days day${if (days > 1) "s" else ""} ago"
        weeks < 4 -> "$weeks week${if (weeks > 1) "s" else ""} ago"
        months < 12 -> "$months month${if (months > 1) "s" else ""} ago"
        else -> "$years year${if (years > 1) "s" else ""} ago"
    }
}

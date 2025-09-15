package com.jmballangca.cropsamarica.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Announcement
import androidx.compose.material.icons.filled.Announcement
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.data.models.rice_field.Announcement
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme


@Composable
fun AnnouncementCard(
    modifier: Modifier = Modifier,
    announcement: Announcement,
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.outlinedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = announcement.title,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                UrgencyBadge(urgency = announcement.urgency)
            }

            // Message
            Text(
                text = announcement.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Date (bottom right)
            Text(
                text = announcement.date.ifBlank { "Today" },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.align(Alignment.End)
            )
        }

    }
}

@Composable
private fun UrgencyBadge(urgency: String) {
    val (bgColor, textColor) = when (urgency.uppercase()) {
        "HIGH" -> MaterialTheme.colorScheme.error to MaterialTheme.colorScheme.onError
        "MEDIUM" -> MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.onTertiary
        else -> MaterialTheme.colorScheme.secondary to MaterialTheme.colorScheme.onSecondary
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = bgColor,
        tonalElevation = 2.dp
    ) {
        Text(
            text = urgency.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Preview
@Composable
private fun AnnouncementCardPreview() {
    CropSamaricaTheme {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AnnouncementCard(
                announcement = Announcement(
                    title = "Water Crops  Reminder haha dasdasdas dasdasdasdasdasdasdasdasdas",
                    message = "Irrigate your rice field today due to hot and dry weather.",
                    urgency = "HIGH",
                    date = "Sep 06, 2025"
                )
            )
            AnnouncementCard(
                announcement = Announcement(
                    title = "Fertilizer Reminder",
                    message = "Apply nitrogen fertilizer this week for better growth.",
                    urgency = "MEDIUM",
                    date = "Sep 06, 2025"
                )
            )
            AnnouncementCard(
                announcement = Announcement(
                    title = "Good Weather",
                    message = "Conditions are favorable, continue regular monitoring.",
                    urgency = "LOW",
                    date = "Sep 06, 2025"
                )
            )
        }
    }
}

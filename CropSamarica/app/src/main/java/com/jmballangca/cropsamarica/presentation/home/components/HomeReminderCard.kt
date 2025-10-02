package com.jmballangca.cropsamarica.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.R
import com.jmballangca.cropsamarica.core.utils.ApplicationCondition
import com.jmballangca.cropsamarica.core.utils.Reminder
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme


@Composable
fun HomeReminderCard(
    modifier: Modifier = Modifier,
    reminder : Reminder,
) {
    OutlinedCard {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.announcement),
                    contentDescription = "Reminder Icon",
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = reminder?.message ?: "",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )


            }
            val announcement = reminder.bestApplicationTime.firstOrNull {
            it.condition == ApplicationCondition.OPTIMAL }
            if (announcement != null) {
                Text(
                    text = "Best  at ${announcement.time}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

    }
}

@Preview
@Composable
private fun HomeReminderCardPrev() {
    CropSamaricaTheme {
        HomeReminderCard(
            reminder = Reminder(
                id = "1",
                message = "This is a reminder message",

            )
        )

    }

}
package com.jmballangca.cropsamarica.presentation.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jmballangca.cropsamarica.data.utils.asNotificationDate
import com.jmballangca.cropsamarica.domain.models.NotificationType
import com.jmballangca.cropsamarica.domain.models.Notifications
import com.jmballangca.cropsamarica.domain.models.SAMPLE_NOTIFICATIONS
import com.jmballangca.cropsamarica.presentation.navigation.VIEW_NOTIFICATION
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme
import java.util.Date


@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    notifications : List<Notifications> = emptyList(),
    viewModel: NotificationsViewModel = hiltViewModel(),

) {
    val notification by rememberUpdatedState(notifications)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events



    NotificationScreen(
        modifier = modifier,
        onBack = {
            navController.popBackStack()
        },
        isLoading = state.isLoading,
        notifications = notification,

        selectedType = state.selectedType,
        onTypeSelected = {
            events(
                NotificationEvents.SelectType(it)
            )
        },
        onNotificationSelected = {
            navController.navigate(VIEW_NOTIFICATION(it))
        }
    )

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    selectedType: NotificationType,
    notifications: List<Notifications> = emptyList(),
    onTypeSelected: (NotificationType) -> Unit,
    onBack: () -> Unit,
    onNotificationSelected: (String) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Notifications")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
        ) {

            if (isLoading) {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            items(notifications,key = { it.id }) {
                NotificationItem(
                    notification = it,
                    onClick = {
                        onNotificationSelected(it.id)
                    }
                )
            }

        }

    }
}

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    notification: Notifications,

    onClick : () -> Unit
) {
    ListItem(
        modifier = modifier.clickable{
            onClick()
        },

        overlineContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = notification.type.displayName, style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Gray
                ))
                Text(text = notification.timestamp?.asNotificationDate() ?: "", style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Gray
                ))
            }
     
        },
        headlineContent = {
            Text(text = notification.message, style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = if (notification.status == "seen") FontWeight.Normal else FontWeight.Bold
            ))
        },
        trailingContent = {
            if (notification.status == "unseen") {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(
                            CircleShape
                        )
                        .background(
                            MaterialTheme.colorScheme.primary
                        )
                ) {  }
            }
        }
    )
}


@Preview
@Composable
private fun NotificationScreenPreview() {
    CropSamaricaTheme {

        NotificationScreen(
            onNotificationSelected = {},
            isLoading = false,
            notifications = SAMPLE_NOTIFICATIONS,
            selectedType = NotificationType.rice_field,
            onTypeSelected = {},
            onBack = {}
        )
    }

}
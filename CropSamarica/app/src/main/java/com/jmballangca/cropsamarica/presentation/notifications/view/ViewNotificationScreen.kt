package com.jmballangca.cropsamarica.presentation.notifications.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jmballangca.cropsamarica.R
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.core.utils.showToast
import com.jmballangca.cropsamarica.data.utils.asNotificationDate
import com.jmballangca.cropsamarica.domain.models.NotificationType
import com.jmballangca.cropsamarica.domain.models.Notifications
import com.jmballangca.cropsamarica.domain.models.SAMPLE_NOTIFICATIONS
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme
import java.sql.Date

@Composable
fun ViewNotificationScreen(
    modifier: Modifier = Modifier,
    id: String,
    navController: NavController,
    viewModel: ViewNotificationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    val oneTimeEvents = viewModel.oneTimeEvents
    val context = LocalContext.current
    LaunchedEffect(oneTimeEvents) {
        oneTimeEvents.collect {
            when(it) {
                is OneTimeEvents.Navigate -> {
                    navController.navigate(it.route)
                }
                OneTimeEvents.NavigateBack -> {
                    navController.popBackStack()
                }
                is OneTimeEvents.ShowToast -> {
                    context.showToast(it.message)
                }
            }
        }

    }
    LaunchedEffect(id) {
        if (id.isNotEmpty()) {
            events(ViewNotificationEvents.LoadNotification(id))
        }
    }
    ViewNotification(
        onBack = {
            navController.popBackStack()
        },
        notification = state.notification,
        isLoading =state.isLoading,
        onDelete = {
            viewModel.events(ViewNotificationEvents.DeleteNotification(it))
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewNotification(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    notification: Notifications?,
    onBack: () -> Unit,
    onDelete : (String) -> Unit,
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = notification?.type?.displayName ?: "Unknown")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (notification != null) {
                        IconButton(
                            onClick = {
                                onDelete(notification.id)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                            )
                        }
                    }

                }
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (notification != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(
                            8.dp
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(80.dp),

                        )
                        Text(
                            text = notification.message,
                            style = MaterialTheme.typography.labelMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Text(
                            text = notification.timestamp?.asNotificationDate() ?: "",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.surfaceDim
                            ),
                            modifier = Modifier.align(Alignment.End)
                        )
                    }

                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
            ) {
                Text(
                    "Reference ID",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    notification?.id ?: "",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color.Gray
                    )
                )
            }
        }
    }
}

@Preview
@Composable
private fun ViewNotificationScreenPrev() {
    CropSamaricaTheme {

        ViewNotification(
            onBack = {},
            notification = SAMPLE_NOTIFICATIONS[0],
            isLoading = false,
            onDelete = {}
        )

    }
}
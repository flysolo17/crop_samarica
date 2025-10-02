package com.jmballangca.cropsamarica.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.jmballangca.cropsamarica.R

import com.jmballangca.cropsamarica.core.ui.AnnouncementCard
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.core.utils.Reminder
import com.jmballangca.cropsamarica.core.utils.shimmer
import com.jmballangca.cropsamarica.data.models.rice_field.RiceFieldWithWeather
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.data.models.rice_field.getRiceStage
import com.jmballangca.cropsamarica.data.models.task.TaskStatus
import com.jmballangca.cropsamarica.data.models.user.User
import com.jmballangca.cropsamarica.domain.models.NotificationStatus
import com.jmballangca.cropsamarica.domain.models.Notifications
import com.jmballangca.cropsamarica.presentation.home.components.HomeReminderCard
import com.jmballangca.cropsamarica.presentation.home.components.NextStageCard
import com.jmballangca.cropsamarica.presentation.home.components.NoTaskFound
import com.jmballangca.cropsamarica.presentation.home.components.ProfileImage
import com.jmballangca.cropsamarica.presentation.home.components.RiceFieldCard
import com.jmballangca.cropsamarica.presentation.home.components.SelectedRiceFieldCard
import com.jmballangca.cropsamarica.presentation.home.components.TaskCard
import com.jmballangca.cropsamarica.presentation.home.components.TaskDialog
import com.jmballangca.cropsamarica.presentation.home.components.WeatherCard
import com.jmballangca.cropsamarica.presentation.navigation.NOTIFICATIONS
import com.jmballangca.cropsamarica.presentation.navigation.SURVEY
import com.jmballangca.cropsamarica.presentation.navigation.TASK
import com.jmballangca.cropsamarica.presentation.navigation.VIEW_CROP_FIELD
import com.jmballangca.cropsamarica.presentation.navigation.WEATHER_FORECAST
import com.jmballangca.cropsamarica.presentation.weather_forecast.ReminderCard
import java.util.Date
import kotlin.collections.filter


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    user: User,
    id: String? = null,
    navController: NavHostController,
    primaryNavController: NavHostController,
    notifications : List<Notifications>,
    onProfileSelected: () -> Unit,
    onCreateCropField: () -> Unit,
    onExpand: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val notification by rememberUpdatedState(notifications)
    val user by rememberUpdatedState(user)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    val oneTimeEvents = viewModel.oneTimeEvents
    LaunchedEffect(oneTimeEvents){
        oneTimeEvents.collect {
            when(it) {
                is OneTimeEvents.Navigate -> {
                    when(it.route) {
                        is SURVEY -> {
                            primaryNavController.navigate(it.route)
                        }
                    }
                }

                OneTimeEvents.NavigateBack -> {}
                is OneTimeEvents.ShowToast -> {}
            }

        }
    }

    LaunchedEffect(id) {
        id?.let {
            events(HomeEvents.GetRiceField(it))
        }
    }
    HomeScreen(
        modifier = modifier,
        user = user,
        riceField = state.riceFieldWithWeather,
        onExpand = {
            onExpand()
        },
        isLoading = state.isLoading,
        onViewCropField = {
            navController.navigate(VIEW_CROP_FIELD(it))
        },
        onNextStage = {
            navController.navigate(SURVEY(
                id = it
            ))
        },
        onProfileSelected = onProfileSelected,
        reminders = state.reminders,
        onViewWeatherForecast = {
            navController.navigate(WEATHER_FORECAST(it))
        },
        navigateToNotification = {
            navController.navigate(NOTIFICATIONS)
        },
        notifications = notification,
    )
}


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    user: User,
    riceField: RiceFieldWithWeather ?,
    onProfileSelected: () -> Unit,
    onExpand : () -> Unit,
    isLoading: Boolean = false,
    onViewCropField : (String) -> Unit,
    reminders : List<Reminder> = emptyList(),
    onNextStage: (String) -> Unit = {},
    onViewWeatherForecast : (String) -> Unit,
    navigateToNotification: () -> Unit,
    notifications : List<Notifications>,
) {
    val field = riceField?.riceField
    val weather = riceField?.weather
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                SelectedRiceFieldCard(
                    modifier = Modifier.shimmer(shimmering = isLoading),
                    selectedRiceField = field,
                ) {
                    onExpand()
                }

                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val badgeCount = notifications.filter {
                        it.status == NotificationStatus.unseen.name
                    }.size
                    BadgedBox(
                        badge = {
                            if (badgeCount > 0) {
                                Badge {
                                    Text(badgeCount.toString())
                                }
                            }
                        }
                    ) {
                        IconButton(
                            onClick = navigateToNotification,
                            modifier = Modifier.shimmer(shimmering = isLoading, shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications"
                            )
                        }
                    }


                    ProfileImage(
                        modifier = Modifier.shimmer(shimmering = isLoading, shape = CircleShape),
                        profile = user.profile,
                        name = user.name,
                        imageSize = 40.dp,
                        onClick = onProfileSelected
                    )
                }
            }
        }
        item {
            AnnouncementCard(
                modifier = Modifier.shimmer(shimmering = isLoading),
                announcement = riceField?.announcements
            )
        }
        items(reminders, key = {it.id}) {
            HomeReminderCard(
                reminder = it,
                modifier = Modifier.shimmer(shimmering = isLoading)
            )
        }

        item {
            WeatherCard(
                modifier = Modifier.shimmer(shimmering = isLoading, shape = MaterialTheme.shapes.large),
                weather = riceField?.weather,
                onClick = {
                    field?.id?.let {
                        onViewWeatherForecast(it)
                    }
                }
            )
        }

        val readyForNextStage = field?.let {
            val currentStage = Date(it.plantedDate).getRiceStage()
            it.stage != RiceStage.MATURE && currentStage != it.stage
        } ?: false

        if (readyForNextStage) {
            item {
                NextStageCard(
                    stage = field.stage,
                    variety = field.variety,
                    onNextStage = {
                        onNextStage(field.id)
                    }
                )
            }
        }

        item {
            RiceFieldCard(
                modifier = Modifier.shimmer(shimmering = isLoading, shape = MaterialTheme.shapes.large),
                riceField = riceField?.riceField,
                onClick = {
                    onViewCropField(field?.id.orEmpty())
                }
            )
        }
        val tasks = riceField?.tasks?.filter {
            it.stage == field?.stage
        } ?: emptyList()
        item {
            Text("Current Task",
                modifier = Modifier.shimmer(shimmering = isLoading),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
        items(tasks, key = {it.id}) {
            TaskCard(
                task = it
            )
        }

        if (tasks.isEmpty() && !isLoading) {
            item {
                NoTaskFound(
                    modifier = Modifier.fillMaxWidth(),
                ){

                }

            }
        }

    }
}



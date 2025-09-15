package com.jmballangca.cropsamarica.presentation.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.jmballangca.cropsamarica.R

import com.jmballangca.cropsamarica.core.anim.BouncingArrow
import com.jmballangca.cropsamarica.core.ui.AnnouncementCard
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.rice_field.RiceFieldWithWeather
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.data.models.rice_field.getHarvestDate
import com.jmballangca.cropsamarica.data.models.rice_field.getRiceStage
import com.jmballangca.cropsamarica.data.models.task.TaskStatus
import com.jmballangca.cropsamarica.data.models.user.User
import com.jmballangca.cropsamarica.domain.models.DailyForecast
import com.jmballangca.cropsamarica.presentation.RiceFieldDateTimeConverter
import com.jmballangca.cropsamarica.presentation.common.LoadingScreen
import com.jmballangca.cropsamarica.presentation.home.components.CreateTaskBottomSheet
import com.jmballangca.cropsamarica.presentation.home.components.NextStageCard
import com.jmballangca.cropsamarica.presentation.home.components.NoRiceFieldContent
import com.jmballangca.cropsamarica.presentation.home.components.NoTaskFound
import com.jmballangca.cropsamarica.presentation.home.components.ProfileImage
import com.jmballangca.cropsamarica.presentation.home.components.RiceFieldCard
import com.jmballangca.cropsamarica.presentation.home.components.RiceStageProgress
import com.jmballangca.cropsamarica.presentation.home.components.SelectedRiceFieldCard
import com.jmballangca.cropsamarica.presentation.home.components.TaskCard
import com.jmballangca.cropsamarica.presentation.home.components.TaskDialog
import com.jmballangca.cropsamarica.presentation.home.components.WeatherCard
import com.jmballangca.cropsamarica.presentation.navigation.SURVEY
import com.jmballangca.cropsamarica.presentation.navigation.TASK
import kotlinx.coroutines.launch
import java.util.Date


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    user: User,
    id: String? = null,
    navController: NavHostController,
    primaryNavController: NavHostController,
    onProfileSelected: () -> Unit,
    onCreateCropField: () -> Unit,
    onExpand: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
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

                OneTimeEvents.NavigateBack -> TODO()
                is OneTimeEvents.ShowToast -> TODO()
            }

        }
    }

    LaunchedEffect(id) {
        id?.let {
            events(HomeEvents.GetRiceField(it))
        }
    }
    when {
        state.isLoading -> {
            LoadingScreen()
        }
        state.riceFieldWithWeather != null && !state.isLoading -> {
            HomeScreen(
                modifier = modifier,
                user = user,
                riceField = state.riceFieldWithWeather!!,
                selectedTaskStatus = state.selectedTaskStatus,
                onExpand = {
                    onExpand()
                },
                onCreateTask = {
                    navController.navigate(TASK)
                },
                events = events,
                onNextStage = {
                    primaryNavController.navigate(SURVEY(
                        id = it
                    ))
                },
                onProfileSelected = onProfileSelected
            )
        }
        else -> {
            NoRiceFieldContent(
                onCreate = onCreateCropField
            )
        }
    }

}


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    user: User,
    riceField: RiceFieldWithWeather,
    selectedTaskStatus: TaskStatus = TaskStatus.PENDING,
    onProfileSelected: () -> Unit,
    onExpand : () -> Unit,
    onCreateTask : () -> Unit,
    events: (HomeEvents) -> Unit,
    onNextStage: (String) -> Unit = {}
) {
    val field = riceField.riceField
    val weather = riceField.weather
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
                field?.let {
                    SelectedRiceFieldCard(
                        selectedRiceField = field,
                    ) {
                        onExpand()
                    }
                } ?: run {
                    Box {}
                }

                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { /**/ }

                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications"
                        )
                    }

                    ProfileImage(
                        profile = user.profile,
                        name = user.name,
                        imageSize = 40.dp,
                        onClick = onProfileSelected
                    )
                }
            }
        }



        riceField.announcements?.let {
            item {
                AnnouncementCard(
                    announcement = it
                )
            }
        }

        weather?.let {
            item {
                WeatherCard(
                    weather = it
                )
            }
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


        field?.let {
            item {
                RiceFieldCard(
                    riceField = it
                )
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedCard(
                    onClick = {

                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.crop),
                            contentDescription = "Crop",
                            modifier = Modifier.size(36.dp)
                        )
                        Text("Crop Management",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }

                OutlinedCard(
                    onClick = {

                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.pest),
                            contentDescription = "Crop",
                            modifier = Modifier.size(36.dp)
                        )
                        Text("Pest and Disease", style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ) )
                    }
                }


                OutlinedCard(
                    onClick = {

                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.weather),
                            contentDescription = "Weather",
                            modifier = Modifier.size(36.dp)
                        )
                        Text("Weather Forecast",  style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ) )
                    }
                }
            }
        }
        val tasks = riceField.tasks.filter {
            it.stage == field?.stage
        }
        item {
            Text("Current Task", style = MaterialTheme.typography.titleMedium)
        }
        items(tasks, key = {it.id}) {
            TaskDialog(
                onStatusChange = { status,result ->
                    events(HomeEvents.OnChangeTaskStatus(
                        id = it.id,
                        taskStatus = status,
                        result = result
                    ))
                },
                onDelete = { result ->
                    events(HomeEvents.OnDeleteTask(
                        id = it.id,
                        result = result
                    ))
                },
                onEdit = { task,result ->
                    events(HomeEvents.OnEditTask(
                        task = task,
                        result = result
                    ))
                },
                task = it,
            )
        }

        if (tasks.isEmpty()) {
            item {
                NoTaskFound(
                    modifier = Modifier.fillMaxWidth(),
                ){
                    CreateTaskBottomSheet { title, description, result ->
                        events(HomeEvents.OnCreateTask(
                            title = title,
                            description = description,
                            result = result
                        ))
                    }
                }

            }
        }

    }
}



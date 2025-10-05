package com.jmballangca.cropsamarica.presentation.weather_forecast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jmballangca.cropsamarica.R
import com.jmballangca.cropsamarica.core.ui.UnknownError
import com.jmballangca.cropsamarica.core.utils.ApplicationCondition
import com.jmballangca.cropsamarica.core.utils.BestApplicationTime
import com.jmballangca.cropsamarica.core.utils.Reminder
import com.jmballangca.cropsamarica.data.models.rice_field.IrrigationType
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStatus

import com.jmballangca.cropsamarica.data.models.weather.SevenDayCondition
import com.jmballangca.cropsamarica.data.models.weather.SevenDayCurrent
import com.jmballangca.cropsamarica.data.models.weather.SevenDayDay
import com.jmballangca.cropsamarica.data.models.weather.SevenDayForecast
import com.jmballangca.cropsamarica.data.models.weather.SevenDayForecastDay
import com.jmballangca.cropsamarica.data.models.weather.SevenDayLocation
import com.jmballangca.cropsamarica.data.models.weather.SevenDayWeatherResponse
import com.jmballangca.cropsamarica.domain.repository.SevenDayWeatherForecast
import com.jmballangca.cropsamarica.domain.utils.toDateOnly
import com.jmballangca.cropsamarica.domain.utils.toDay
import com.jmballangca.cropsamarica.presentation.common.LoadingScreen
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.RiceVariety
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.SoilTypes
import com.jmballangca.cropsamarica.presentation.home.components.WeatherCard
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun WeatherForecastScreen(
    modifier: Modifier = Modifier,
    id: String,
    primaryNavController: NavController,
    viewModel: WeatherForecastViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    val scope = rememberCoroutineScope()
    LaunchedEffect(id) {
        if (id.isNotEmpty()) {
            events(WeatherForecastEvents.LoadWeatherForecast(id))
            events(WeatherForecastEvents.OnGetReminders(id))
        }
    }
    LaunchedEffect(state.weatherForecast, state.reminders) {
        val forecast = state.weatherForecast
        if (forecast?.sevenDayWeatherResponse != null &&
            forecast.riceField != null &&
            state.reminders.isEmpty()
        ) {
            events(
                WeatherForecastEvents.OnGenerateReminder(
                    forecast.sevenDayWeatherResponse,
                    forecast.riceField
                )
            )
        }
    }
    when  {
        state.isGeneratingReminder || state.isLoading -> {
            LoadingScreen(
                title = "Aya is analyzing the data might take a moment..."
            )
        }
        state.error != null -> {
            UnknownError(
                message = state.error.toString(),
                onBack = {
                    primaryNavController.popBackStack()
                }
            )
        }
        else -> {
            WeatherForecastScreen(
                modifier = modifier,
                isLoading = state.isLoading,
                reminders = state.reminders,
                isGeneratingReminder = state.isGeneratingReminder,
                weatherForecast = state.weatherForecast,
                aiReminders = state.aiReminders,
                onNotify = {
                    events(WeatherForecastEvents.OnNotify(it))
                },
                error = state.error,
                onBackClick = {
                    primaryNavController.popBackStack()
                }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherForecastScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    isGeneratingReminder: Boolean,
    weatherForecast: SevenDayWeatherForecast?,
    aiReminders : List<Reminder>,
    error: String?,
    reminders : List<Reminder>,
    onBackClick: () -> Unit,
    onNotify : (Reminder) -> Unit

) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Weather Forecast")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                    ) {
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isLoading) {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            if (error != null && !isLoading) {
                item {
                    UnknownError (
                        message = error,
                        onBack = onBackClick
                    )
                }
            }
            if (weatherForecast != null && !isLoading) {
                item {
                    val location = weatherForecast.sevenDayWeatherResponse?.location
                    val current = weatherForecast.sevenDayWeatherResponse?.current
                    CurrentForecastCard(
                        location = location,
                        current = current
                    )
                }
            }
            item {
                Text(
                    text = "Reminders",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            items(reminders, key = {it.id}) {

                ReminderCard(
                    reminder = it,
                    isNotified = true
                )
            }
            item(
            ) {
                Text(
                    text = "Next 6 days",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            val nextSixDays = weatherForecast?.sevenDayWeatherResponse?.forecast?.forecastday?.takeLast(2) ?: emptyList()
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        nextSixDays.forEach {
                            Column(
                                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {

                                Text(it.date.toDay())
                                AsyncImage(
                                    model = "https:${it.day.condition.icon}",
                                    contentDescription = "Weather Icon",
                                    modifier = Modifier.size(40.dp),
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                )
                                Text(
                                    text = "${it.day.avgtemp_c}°C",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Aya Reminders")
                    if (isGeneratingReminder) {
                        Text("Generating...",style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray
                        ))
                    }

                }

            }
            val aira = aiReminders.filter { reminder ->
                reminders.none { it.message == reminder.message }
            }

            items(aira, key = {it.id}) {
                ReminderCard(reminder = it, isNotified = false, onNotify = {
                    onNotify(it)
                })
            }
        }
    }
}

@Composable
fun ForecastDaycard(
    modifier: Modifier = Modifier,
    day: SevenDayForecastDay
) {
    val current = day.day
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column {
                Text(
                    text = day.date,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${current.avgtemp_c}°C",
                    style = MaterialTheme.typography.titleMedium
                )
            }

        }
    }
    
}



@Composable
fun ReminderCard(
    reminder: Reminder,
    modifier: Modifier = Modifier,
    isNotified : Boolean = true,
    onNotify: () -> Unit = {}
) {
    val dateFormatter = remember { SimpleDateFormat("MMM dd", Locale.getDefault()) }
    val dayFormatter = remember { SimpleDateFormat("EEEE", Locale.getDefault()) }

    val date = dateFormatter.format(reminder.reminderDate)
    val day = dayFormatter.format(reminder.reminderDate)
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text("${date}", style = MaterialTheme.typography.titleMedium)
                Text("${day}", style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = reminder.message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                ),
            )
            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(
                        rememberScrollState()
                    ),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val time = reminder.bestApplicationTime
                time.forEach {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = it.condition.color,
                                    shape = CircleShape
                                ),
                            contentAlignment = androidx.compose.ui.Alignment.Center
                        ) {
                            Icon(
                                imageVector = it.condition.image,
                                contentDescription = it.condition.name,
                                modifier = Modifier.size(16.dp),
                            )

                        }
                        Text(
                            text = it.time,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            if (!isNotified) {
                TextButton(
                    onClick = onNotify,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Back"
                        )
                        Text(
                            text = "Notify me",
                            style = MaterialTheme.typography.bodySmall
                        )

                    }
                }
            }
        }
    }

}

@Composable
fun CurrentForecastCard(
    modifier: Modifier = Modifier,
    location: SevenDayLocation?,
    current: SevenDayCurrent ?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Back"
                    )
                    Text(
                        text = "${location?.name} ${location?.region}",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                Text(
                    text = location?.localtime?.toFormattedDate() ?: "",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "${current?.temp_c}°C",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = "${current?.condition?.text}",
                    style = MaterialTheme.typography.labelSmall
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.wind),
                            contentDescription = "Wind Speed",
                            modifier = Modifier.size(16.dp),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        Text(
                            text = "${current?.wind_kph} km/h",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.humidity),
                            contentDescription = "Wind Speed",
                            modifier = Modifier.size(16.dp),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        Text(
                            text = "${current?.humidity}%",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }

            }
            AsyncImage(
                model = "https:${current?.condition?.icon}",
                contentDescription = "Weather Icon",
                modifier = Modifier.size(80.dp),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.cloudy),
                error = painterResource(id = R.drawable.cloudy)
            )
        }
    }
    
}

/***
 * Converts a string date like "2025-09-20 23:10"
 * into a formatted string like "Saturday, Sep 20".
 *
 * @receiver String - e.g. "2025-09-20 23:10"
 * @return String - formatted as "Saturday, Sep 20"
 */
fun String.toFormattedDate(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE, MMM dd", Locale.getDefault())
        val date = inputFormat.parse(this)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        this // fallback: return original string if parsing fails
    }
}
@Preview
@Composable
private fun WeatherForecastScreenPrev() {
    val reminders = listOf(
        Reminder(
            id = "reminder_1",
            riceFieldId = "uePgWXgrkOVrxo086Keg",
            message = "Apply herbicide for early weed control during the morning window to avoid heavy rains and strong winds later in the day.",
            bestApplicationTime = listOf(
                BestApplicationTime("6 AM", ApplicationCondition.OPTIMAL),
                BestApplicationTime("7 AM", ApplicationCondition.OPTIMAL),
                BestApplicationTime("8 AM", ApplicationCondition.OPTIMAL),
                BestApplicationTime("9 AM", ApplicationCondition.OPTIMAL),
                BestApplicationTime("10 AM", ApplicationCondition.OPTIMAL),
                BestApplicationTime("11 AM", ApplicationCondition.MODERATE),
                BestApplicationTime("12 PM", ApplicationCondition.MODERATE),
                BestApplicationTime("1 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("2 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("3 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("4 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("5 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("6 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("7 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("8 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("9 PM", ApplicationCondition.UNFAVORABLE)
            ),
            reminderDate = Date(1737302400000) // Mon Jan 20 2025
        ),
        Reminder(
            id = "reminder_2",
            riceFieldId = "uePgWXgrkOVrxo086Keg",
            message = "Inspect and clear your drainage canals and gates in the evening to prepare for or manage the persistent heavy rainfall.",
            bestApplicationTime= listOf(
                BestApplicationTime("6 AM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("7 AM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("8 AM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("9 AM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("10 AM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("11 AM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("12 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("1 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("2 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("3 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("4 PM", ApplicationCondition.UNFAVORABLE),
                BestApplicationTime("5 PM", ApplicationCondition.MODERATE),
                BestApplicationTime("6 PM", ApplicationCondition.OPTIMAL),
                BestApplicationTime("7 PM", ApplicationCondition.OPTIMAL),
                BestApplicationTime("8 PM", ApplicationCondition.OPTIMAL),
                BestApplicationTime("9 PM", ApplicationCondition.MODERATE)
            ),
            reminderDate = Date(1737388800000) // Tue Jan 21 2025
        )
    )
    val forecast3 = SevenDayWeatherForecast(
        riceField = RiceField(
            id = "uePgWXgrkOVrxo086Keg",
            uid = "F6XG0qOiHXZQj26On7r28rGobY43",
            name = "Bolinao Farm",
            stage = RiceStage.SEEDLING,
            location = "Bolinao Pangasinan",
            plantedDate = 1757548800000,
            expectedHarvestDate = 1767101521494,
            variety = RiceVariety.NSIC_RC9,
            status = RiceStatus.NEEDS_ATTENTION,
            areaSize = 1.0,
            irrigationType = IrrigationType.PUMP_IRRIGATION,
            soilType = SoilTypes.CLAY_LOAM,
            createdAt = Date(1757770321000),
            updatedAt = Date(1757770321000)
        ),

        sevenDayWeatherResponse = SevenDayWeatherResponse(
            location = SevenDayLocation(
                name = "Bolinao",
                region = "Pangasinan",
                country = "Philippines",
                localtime = "2025-09-20 23:10"
            ),
            current = SevenDayCurrent(
                temp_c = 27.0,
                condition = SevenDayCondition(
                    text = "Light rain shower",
                    icon = "//cdn.weatherapi.com/weather/64x64/night/353.png",


                ),
                wind_kph = 10.0,
                humidity = 80

            ),
            forecast = SevenDayForecast(
                forecastday = listOf(
                    SevenDayForecastDay(
                        date = "2025-09-20",

                        day = SevenDayDay(
                            condition = SevenDayCondition(
                                text = "Moderate rain",
                                icon = "//cdn.weatherapi.com/weather/64x64/day/302.png",

                            ),
                            avgtemp_c = 27.0,

                        ),


                    )
                )
            )
        )
    )
    WeatherForecastScreen(
        weatherForecast = forecast3,
        isLoading = false,
        reminders = reminders,
        isGeneratingReminder = false,
        aiReminders = reminders,
        error = null,
        onBackClick = { },
        onNotify = { }
    )
}
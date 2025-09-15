package com.jmballangca.cropsamarica.presentation.home.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jmballangca.cropsamarica.domain.models.DailyForecast
import com.jmballangca.cropsamarica.domain.models.sampleDailyForecast
import com.jmballangca.cropsamarica.domain.utils.toProperTime
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun WeatherCard(
    modifier: Modifier = Modifier,
    weather: DailyForecast
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text(
                    text = weather.location,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location"
                )
            }
            Text(
                text = weather.currentTemp,
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = weather.condition.text,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "${weather.highLow} ${weather.feelsLike}",
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(
                        top = 16.dp
                    )
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                weather.hourly.forEach {
                    Column(
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = it.time.toProperTime(),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onTertiary
                            )
                        )
                        AsyncImage(
                            model = "https:${it.condition.icon}",
                            contentDescription = "Weather Icon",
                            modifier = Modifier.size(24.dp),
                            contentScale = androidx.compose.ui.layout.ContentScale.Fit
                        )
                        Text(
                            text = it.temp,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

            }
        }
    }
}



@Preview
@Composable
private fun WeatherCardPrev() {
    CropSamaricaTheme {
        WeatherCard(
            weather = sampleDailyForecast
        )
    }
}


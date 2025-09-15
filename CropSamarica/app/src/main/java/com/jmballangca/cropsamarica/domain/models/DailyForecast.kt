package com.jmballangca.cropsamarica.domain.models

import com.jmballangca.cropsamarica.data.models.weather.Condition
import com.jmballangca.cropsamarica.data.models.weather.Current
import com.jmballangca.cropsamarica.data.models.weather.Forecast
import com.jmballangca.cropsamarica.data.models.weather.Location
import com.jmballangca.cropsamarica.data.models.weather.WeatherApiResponse
import com.jmballangca.cropsamarica.data.models.weather.WeatherBulkApiResponse
import com.jmballangca.cropsamarica.domain.utils.getLocation
import com.jmballangca.cropsamarica.domain.utils.toDateOnly
import kotlinx.serialization.Serializable



fun WeatherBulkApiResponse.toDailyForecastUI(): List<DailyForecast> {
    val dailyForecasts = mutableListOf<DailyForecast>()
    this.bulk.map {
        val location = it.query.location
        val current = it.query.current
        val forecast = it.query.forecast
        val weatherApiResponse = WeatherApiResponse(
            location = location,
            current = current,
            forecast = forecast
        )
        dailyForecasts.add(
            weatherApiResponse.toDailyForecastUI()
        )
    }
    return dailyForecasts
}

@Serializable
data class DailyForecast(
    var id: String = "",
    var location: String = "",
    val date: String = "",
    val currentTemp: String = "",
    val condition: Condition = Condition(),
    val feelsLike: String = "",
    val highLow: String = "",
    val description: String = "",
    val hourly: List<HourlyForecast> = emptyList()
)

val sampleDailyForecast = DailyForecast(
    id = "df_001",
    location = "New York City",
    date = "2025-08-27",
    currentTemp = "26°C",
    condition = Condition(
        text = "Sunny",
        icon = "https://cdn.weatherapi.com/weather/64x64/night/116.png"
    ),
    feelsLike = "28°C",
    highLow = "30°C / 22°C",
    description = "Clear skies throughout the day with warm afternoon temperatures.",
    hourly = listOf(
        HourlyForecast(
            time = "08:00",
            temp = "22°C",
            condition = Condition("Sunny", "https://cdn.weatherapi.com/weather/64x64/night/116.png")
        ),
        HourlyForecast(
            time = "12:00",
            temp = "26°C",
            condition = Condition("Sunny", "https://cdn.weatherapi.com/weather/64x64/night/116.png")
        ),
        HourlyForecast(
            time = "16:00",
            temp = "30°C",
            condition = Condition("Partly Cloudy", "https://cdn.weatherapi.com/weather/64x64/night/116.png")
        ),
        HourlyForecast(
            time = "20:00",
            temp = "24°C",
            condition = Condition("Clear Night", "https://cdn.weatherapi.com/weather/64x64/night/116.png")
        )
    )
)

fun WeatherApiResponse.toDailyForecastUI(): DailyForecast {
    val forecastDay = forecast.forecastday.firstOrNull()

    return DailyForecast(
        location = location.getLocation(),
        date = this.location.localtime.toDateOnly(),
        currentTemp = "${current.temp_c}°",
        condition = Condition(
            text = current.condition.text,
            icon = current.condition.icon
        ),
        feelsLike = "Feels like ${current.feelslike_c}°",
        highLow = "${forecastDay?.day?.maxtemp_c}° / ${forecastDay?.day?.mintemp_c}°",
        description = forecastDay?.day?.condition?.text ?: "",
        hourly = forecastDay?.hour?.map { hour ->
            HourlyForecast(
                time = hour.time.substringAfter(" "),
                temp = "${hour.temp_c}°",
                condition = Condition(
                    text = hour.condition.text,
                    icon = hour.condition.icon
                ),
                chanceOfRain = hour.chance_of_rain
            )
        } ?: emptyList()
    )
}



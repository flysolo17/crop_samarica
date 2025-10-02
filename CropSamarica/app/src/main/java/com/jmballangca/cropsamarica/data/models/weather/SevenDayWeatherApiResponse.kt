package com.jmballangca.cropsamarica.data.models.weather




data class SevenDayWeatherResponse(
    val location: SevenDayLocation,
    val current: SevenDayCurrent,
    val forecast: SevenDayForecast
)

data class SevenDayLocation(
    val name: String,
    val region: String,
    val country: String,
    val localtime: String
)

data class SevenDayCurrent(
    val temp_c: Double,
    val condition: SevenDayCondition,
    val wind_kph: Double,
    val humidity: Int
)

data class SevenDayCondition(
    val text: String,
    val icon: String
)

data class SevenDayForecast(
    val forecastday: List<SevenDayForecastDay>
)

data class SevenDayForecastDay(
    val date: String,
    val day: SevenDayDay
)

data class SevenDayDay(
    val avgtemp_c: Double,
    val condition: SevenDayCondition
)

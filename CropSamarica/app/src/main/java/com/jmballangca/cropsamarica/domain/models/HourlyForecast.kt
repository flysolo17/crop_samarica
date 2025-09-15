package com.jmballangca.cropsamarica.domain.models

import com.jmballangca.cropsamarica.data.models.weather.Condition
import kotlinx.serialization.Serializable

@Serializable
data class HourlyForecast(
    val time: String = "",
    val temp: String = "",
    val condition: Condition  = Condition(),
    val iconUrl: String = "",
    val chanceOfRain: Int = 0
)
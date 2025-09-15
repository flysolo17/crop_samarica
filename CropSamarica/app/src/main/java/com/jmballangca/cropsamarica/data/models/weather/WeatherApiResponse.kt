package com.jmballangca.cropsamarica.data.models.weather

import kotlinx.serialization.Serializable

@Serializable
data class WeatherApiResponse(
    val location: Location,
    val current: Current,
    val forecast: Forecast
)
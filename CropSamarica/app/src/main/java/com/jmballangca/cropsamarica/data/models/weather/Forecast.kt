package com.jmballangca.cropsamarica.data.models.weather

import kotlinx.serialization.Serializable

@Serializable
data class Forecast(val forecastday: List<ForecastDay>)

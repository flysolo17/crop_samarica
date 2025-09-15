package com.jmballangca.cropsamarica.data.models.weather

import kotlinx.serialization.Serializable

@Serializable
data class ForecastDay(
    val day: Day,
    val hour: List<Hour>
)
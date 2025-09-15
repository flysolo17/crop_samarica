package com.jmballangca.cropsamarica.data.models.weather

import kotlinx.serialization.Serializable


// Kotlin data classes as provided by the user, now with @Serializable annotation
@Serializable
data class Weather(
    val date: String,
    val location: Location,
    val temp: String,
    val condition: Condition,
    val humidity: String,
    val wind: String,
)








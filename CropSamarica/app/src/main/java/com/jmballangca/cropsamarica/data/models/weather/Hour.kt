package com.jmballangca.cropsamarica.data.models.weather

import kotlinx.serialization.Serializable

@Serializable
data class Hour(
    val time: String,
    val temp_c: Double,
    val chance_of_rain: Int,
    val condition: Condition
)
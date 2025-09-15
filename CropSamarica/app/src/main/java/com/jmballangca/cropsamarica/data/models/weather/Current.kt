package com.jmballangca.cropsamarica.data.models.weather

import kotlinx.serialization.Serializable

@Serializable
data class Current(
    val temp_c: Double,
    val feelslike_c: Double,
    val condition: Condition
)


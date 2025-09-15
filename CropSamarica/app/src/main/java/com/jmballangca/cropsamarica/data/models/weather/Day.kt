package com.jmballangca.cropsamarica.data.models.weather

import kotlinx.serialization.Serializable

@Serializable
data class Day(
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val condition: Condition
)

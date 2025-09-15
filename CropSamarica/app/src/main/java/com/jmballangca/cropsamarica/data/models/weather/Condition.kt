package com.jmballangca.cropsamarica.data.models.weather

import kotlinx.serialization.Serializable

@Serializable
data class Condition(
    val text: String = "",
    val icon: String = ""
)

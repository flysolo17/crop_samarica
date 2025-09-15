package com.jmballangca.cropsamarica.data.models.weather

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val name: String,
    val region: String,
    val country: String,
    val localtime : String,
)

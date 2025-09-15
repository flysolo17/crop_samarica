package com.jmballangca.cropsamarica.data.models.weather

import kotlinx.serialization.Serializable


@Serializable
data class BulkLocation(
    val locations : List<LocationName>
)
@Serializable
data class LocationName(
    val q : String
)
@Serializable
data class WeatherBulkApiResponse(
    val bulk: List<BulkWeatherItem>
)
@Serializable
data class BulkWeatherItem(
    val query: LocationQuery,
)
@Serializable
data class LocationQuery(
    val q : String,
    val location : Location,
    val current : Current,
    val forecast : Forecast,
)
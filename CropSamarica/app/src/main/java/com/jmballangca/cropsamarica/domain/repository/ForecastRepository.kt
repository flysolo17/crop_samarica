package com.jmballangca.cropsamarica.domain.repository

import com.jmballangca.cropsamarica.domain.models.DailyForecast
import java.util.Date

interface ForecastRepository {

    suspend fun getDailyForecast(
        location: String,
        days: Int = 1
    ) : Result<DailyForecast>

    suspend fun getWeeklyForecast(
        location: String,
        days: Int = 7
    ) : Result<List<DailyForecast>>

    suspend fun getWeather(
        location: String,
        date : Date = Date()
    ) : Result<DailyForecast>
}
package com.jmballangca.cropsamarica.domain.repository.impl

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.jmballangca.cropsamarica.data.service.WeatherApiService
import com.jmballangca.cropsamarica.domain.models.DailyForecast
import com.jmballangca.cropsamarica.domain.models.toDailyForecastUI
import com.jmballangca.cropsamarica.domain.repository.ForecastRepository
import com.jmballangca.cropsamarica.domain.utils.toDateOnly
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val firestore: FirebaseFirestore
): ForecastRepository {
    private val weatherRef = firestore.collection("weather")
    override suspend fun getDailyForecast(
        location: String,
        days: Int
    ): Result<DailyForecast> {
        return try {
            val response = weatherApiService.getWeather(location,days)
            if (response.isSuccessful) {
                val weather = response.body()
                if (weather != null) {
                    Log.d(TAG, "getDailyForecast: $weather")
                    Result.success(weather.toDailyForecastUI())
                } else {
                    Log.d(TAG, "getDailyForecast: Weather data is null")
                    Result.failure(Exception("Weather data is null"))
                }
            } else {
                Log.d(TAG, "getDailyForecast: Failed to fetch weather data")
                Result.failure(Exception("Failed to fetch weather data"))
            }
        } catch (e: Exception) {
            Log.d(TAG, "getDailyForecast: ${e.message}")
            Result.failure(e)
        }
    }
    override suspend fun getWeeklyForecast(
        location: String,
        days: Int
    ): Result<List<DailyForecast>> {
        return Result.success(emptyList())
    }
    override suspend fun getWeather(
        location: String,
        date: Date
    ): Result<DailyForecast> {
        return try {

            val querySnapshot = weatherRef
                .whereEqualTo("location", location)
                .whereEqualTo("date", date.toDateOnly())
                .limit(1)
                .get()
                .await()

            val forecast = querySnapshot.documents.firstOrNull()
                ?.toObject(DailyForecast::class.java)

            if (forecast != null) {
                Result.success(forecast)
            } else {
                val result  = weatherApiService.getWeather(location)
                if (result.isSuccessful) {
                    val weather = result.body()
                    if (weather != null) {
                        val weather = weather.toDailyForecastUI()
                        val id = weatherRef.document().id
                        weather.id = id
                        weather.location = location
                        weatherRef.document(id).set(weather).await()
                        Result.success(weather)
                    } else {
                        Result.failure(Exception("Weather data is null"))
                    }
                }
                Result.failure(Exception("No forecast found for $location on"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    companion object {
        const val TAG = "ForecastRepository"
    }

}
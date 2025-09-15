package com.jmballangca.cropsamarica.data.service


import com.jmballangca.cropsamarica.BuildConfig
import com.jmballangca.cropsamarica.data.models.weather.BulkLocation
import com.jmballangca.cropsamarica.data.models.weather.Current
import com.jmballangca.cropsamarica.data.models.weather.Forecast
import com.jmballangca.cropsamarica.data.models.weather.Location
import com.jmballangca.cropsamarica.data.models.weather.WeatherApiResponse
import com.jmballangca.cropsamarica.data.models.weather.WeatherBulkApiResponse
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface WeatherApiService {

    @GET("/v1/forecast.json")
    suspend fun getWeather(
        @Query("q") location: String,
        @Query("days") days: Int = 7,
        @Query("key") key: String = BuildConfig.WEATHER_SECRET
    ): Response<WeatherApiResponse>


    @POST("/v1/forecast.json")
    suspend fun getBulk(
        @Query("q") location: String = "BULK",
        @Query("key") key: String = BuildConfig.WEATHER_SECRET,
        @Body request: BulkLocation
    ) : Response<WeatherBulkApiResponse>
}
package com.jmballangca.cropsamarica.data.models.rice_field

import com.jmballangca.cropsamarica.data.models.task.Task
import com.jmballangca.cropsamarica.domain.models.DailyForecast


data class RiceFieldWithWeather(
    val riceField: RiceField ? = null,
    val weather: DailyForecast? = null,
    val tasks : List<Task> = emptyList(),
    val announcements : Announcement? = null
)
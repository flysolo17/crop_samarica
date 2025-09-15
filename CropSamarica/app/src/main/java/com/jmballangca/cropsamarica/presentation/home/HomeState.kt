package com.jmballangca.cropsamarica.presentation.home
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.rice_field.RiceFieldWithWeather
import com.jmballangca.cropsamarica.data.models.task.TaskStatus
import com.jmballangca.cropsamarica.domain.models.DailyForecast
import com.jmballangca.cropsamarica.domain.models.RecommendationResult

data class HomeState(
    val isLoading : Boolean = false,
    val riceFieldWithWeather: RiceFieldWithWeather? = null,
    val selectedTaskStatus: TaskStatus = TaskStatus.PENDING,
    val recommendationResult: RecommendationResult ? = null
)


package com.jmballangca.cropsamarica.domain.repository

import android.net.Uri
import com.jmballangca.cropsamarica.core.utils.Reminder
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.data.models.weather.SevenDayWeatherResponse
import com.jmballangca.cropsamarica.domain.models.DailyForecast
import com.jmballangca.cropsamarica.domain.models.Recommendation
import com.jmballangca.cropsamarica.domain.models.RecommendationResult
import com.jmballangca.cropsamarica.domain.models.questions.Question
import com.jmballangca.cropsamarica.domain.models.questions.QuestionsWithCurrentStage
import com.jmballangca.cropsamarica.presentation.create_crop_field.CreateCropFieldForm
import com.jmballangca.cropsamarica.presentation.survey.QuestionWithAnswers


data class SevenDayWeatherForecast(
    val riceField: RiceField ? = null,
    val sevenDayWeatherResponse: SevenDayWeatherResponse ? = null,
)
interface AyaRepository {
    suspend fun generateCropField(
        data: CreateCropFieldForm
    ) : Result<List<Recommendation>>

    suspend fun generateRecommendation(
        data : RiceField,
        imageUri : Uri? = null,
    ) : Result<RecommendationResult>


    suspend fun generateSurveyForLastStage(
        id : String,
    ) : Result<QuestionsWithCurrentStage>


    suspend fun generateRecommendationForNextStageBasedOnTheSurvey(
        riceField : RiceField,
        survey : List<QuestionWithAnswers>,
        imageUri : Uri? = null
    ) : Result<RecommendationResult>


    suspend fun generateWeatherForecast(
        id : String
    ) : Result<SevenDayWeatherForecast>


    suspend fun generateReminder(
        riceField : RiceField,
        weather : SevenDayWeatherResponse
    ) : Result<List<Reminder>>
}
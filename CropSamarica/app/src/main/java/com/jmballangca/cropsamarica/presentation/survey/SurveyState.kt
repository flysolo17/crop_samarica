package com.jmballangca.cropsamarica.presentation.survey

import android.net.Uri
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.domain.models.RecommendationResult
import com.jmballangca.cropsamarica.domain.models.questions.Question

data class QuestionWithAnswers(
    val question: Question,
    val answer : String
)

data class SurveyState(
    val isLoading : Boolean = false,
    val questions  : List<Question> = emptyList(),
    val questionWithAnswers : List<QuestionWithAnswers> = emptyList(),
    val selectedImage : Uri? = null,
    val riceField: RiceField? = null,
    val recommendationResult: RecommendationResult? = null,
    val isCreatingTask : Boolean = false,

)
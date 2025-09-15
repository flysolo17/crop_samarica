package com.jmballangca.cropsamarica.presentation.survey

import android.net.Uri
import com.jmballangca.cropsamarica.data.models.task.Task
import com.jmballangca.cropsamarica.presentation.create_crop_field.CreateCropFieldEvents
import com.jmballangca.cropsamarica.presentation.home.HomeEvents


sealed interface SurveyEvents {
    data class OnGenerateSurvey(val id: String) : SurveyEvents
    data class OnChangeAnswer(val index: Int, val answer: String) : SurveyEvents
    data class OnImageChange(
        val image : Uri
    ) : SurveyEvents


    data object OnSubmit : SurveyEvents
    data class OnCreateTask(
        val tasks : List<Task>
    ) : SurveyEvents
}
package com.jmballangca.cropsamarica.presentation.survey

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.data.models.rice_field.getRiceStage
import com.jmballangca.cropsamarica.data.models.task.Task

import com.jmballangca.cropsamarica.domain.repository.AyaRepository
import com.jmballangca.cropsamarica.domain.repository.TaskRepository
import com.jmballangca.cropsamarica.presentation.navigation.SURVEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class SurveyViewModel  @Inject constructor(
    private val ayaRepository: AyaRepository,
    private val taskRepository: TaskRepository,

): ViewModel() {
    private var _state = MutableStateFlow(SurveyState())
    val state = _state.asStateFlow()

    private var _oneTimeEvents = Channel<OneTimeEvents>()
    val oneTimeEvents = _oneTimeEvents.receiveAsFlow()

    fun events(e : SurveyEvents) {
        when(e) {
            is SurveyEvents.OnGenerateSurvey -> generateSurvey(e.id)
            is SurveyEvents.OnChangeAnswer -> answerChanged(e.index,e.answer)
            is SurveyEvents.OnImageChange -> {
                _state.value = _state.value.copy(
                    selectedImage = e.image
                )
            }

            SurveyEvents.OnSubmit -> submit()
            is SurveyEvents.OnCreateTask -> createTasks(e.tasks)
        }
    }

    private fun submit() {
        val riceField = _state.value.riceField
        if (riceField == null) {
            return
        }
        _state.value = _state.value.copy(
            isLoading = true
        )

        viewModelScope.launch {
            ayaRepository.generateRecommendationForNextStageBasedOnTheSurvey(
                riceField = riceField,
                survey = state.value.questionWithAnswers,
                imageUri = state.value.selectedImage
            ).onSuccess {
                _state.value = _state.value.copy(
                    isLoading = false,
                    recommendationResult = it
                )
                Log.d("SurveyViewModel", "submit: $it")
            }.onFailure {
                _state.value = _state.value.copy(
                    isLoading = false
                )
                Log.e("SurveyViewModel", "submit: $it", it)
            }

        }
    }

    private fun answerChanged(index: Int, answer: String) {
        _state.value = _state.value.copy(
            questionWithAnswers = _state.value.questionWithAnswers.mapIndexed { i, questionWithAnswers ->
                if (i == index) {
                    questionWithAnswers.copy(answer = answer)
                } else {
                    questionWithAnswers
                }
            }
        )
    }

    private fun generateSurvey(
        id: String
    ) {
        viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true
            )
            val result = ayaRepository.generateSurveyForLastStage(id = id)
            result.onSuccess { questions ->
                Log.d(
                    "SurveyViewModel",
                    "generateSurvey: $questions"
                )
                _state.value = _state.value.copy(
                    questions = questions.questions,
                    riceField = questions.riceField,
                    questionWithAnswers = questions.questions.map {
                        QuestionWithAnswers(
                            question = it,
                            answer = ""
                        )
                    },
                    isLoading = false
                )
            }.onFailure {
                Log.e(
                    "SurveyViewModel",
                    "generateSurvey: $it",
                    it
                )
                _state.value = _state.value.copy(
                    questions = emptyList(),
                    isLoading = false
                )
            }
        }
    }

    private fun createTasks(tasks: List<Task>) {
        val nextStage = Date(
            _state.value.riceField?.plantedDate ?: System.currentTimeMillis()
        ).getRiceStage()
        val newTasks = tasks.map {
            it.copy(
                stage = nextStage
            )
        }
        _state.value = _state.value.copy(isCreatingTask = true)
        viewModelScope.launch {
            taskRepository.insertAll(newTasks).onSuccess {
                _state.value = _state.value.copy(isCreatingTask = false)
                _oneTimeEvents.send(OneTimeEvents.NavigateBack)
            }.onFailure {
                _state.value = _state.value.copy(isCreatingTask = false)
            }
        }
    }
}
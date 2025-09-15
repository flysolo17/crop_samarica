package com.jmballangca.cropsamarica.presentation.survey

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.core.utils.showToast
import com.jmballangca.cropsamarica.domain.models.questions.Question
import com.jmballangca.cropsamarica.domain.models.questions.QuestionType
import com.jmballangca.cropsamarica.presentation.common.LoadingScreen
import com.jmballangca.cropsamarica.presentation.create_crop_field.CreateCropFieldEvents
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.ImagePicker
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.RecommendationDialog
import com.jmballangca.cropsamarica.presentation.survey.components.LongAnswer
import com.jmballangca.cropsamarica.presentation.survey.components.MultipleChoice
import com.jmballangca.cropsamarica.presentation.survey.components.ShortAnswer
import com.jmballangca.cropsamarica.presentation.survey.components.SingleChoice
import com.jmballangca.cropsamarica.presentation.survey.components.TrueOrFalse


@Composable
fun SurveyScreen(
    modifier: Modifier = Modifier,
    id: String,
    viewModel: SurveyViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events

    val isLoading = state.isLoading
    val questions = state.questionWithAnswers
    val selectedImage = state.selectedImage
    LaunchedEffect(Unit) {
        viewModel.events(SurveyEvents.OnGenerateSurvey(id))
    }

    val oneTimeEvents = viewModel.oneTimeEvents
    val context = LocalContext.current
    LaunchedEffect(oneTimeEvents) {
        oneTimeEvents.collect {
            when (it) {
                is OneTimeEvents.Navigate -> {
                    navHostController.navigate(it.route)
                }
                OneTimeEvents.NavigateBack -> {
                    navHostController.popBackStack()
                }
                is OneTimeEvents.ShowToast -> {
                    context.showToast(it.message)
                }
            }
        }
    }
    val reccomendations = state.recommendationResult
    if (reccomendations != null) {
        RecommendationDialog(
            recommendations = reccomendations,
            onDismiss = {
                navHostController.popBackStack()
            },
            onCreateTask = {
                events(SurveyEvents.OnCreateTask(it))
            }
        )
    }
    when {
        isLoading -> {
            LoadingScreen()
        }
        !isLoading && questions.isEmpty()  -> {
            Text(text = "No questions found")
        }
        else -> {
            SurveyScreen(
                modifier = modifier,
                selectedImage = selectedImage,
                questions = questions,
                events = events
            )
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurveyScreen(
    modifier: Modifier = Modifier,
    selectedImage : Uri?,
    isLoading : Boolean = false,
    questions: List<QuestionWithAnswers>,
    events: (SurveyEvents) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Ready for next stage?")
                }
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            contentPadding = PaddingValues(
                16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(
                items = questions,
                key = { index, _ -> index }
            ) { index, question ->
                when(question.question.type) {
                    QuestionType.MULTIPLE_CHOICE -> MultipleChoice(
                        question = question.question,
                        selectedAnswers = question.answer.split(","),
                        onAnswerSelected = {
                            events(SurveyEvents.OnChangeAnswer(
                                index = index,
                                answer = it
                            ))
                        }
                    )
                    QuestionType.SINGLE_CHOICE -> SingleChoice(
                        question = question.question,
                        selectedAnswer = question.answer,
                        onAnswerSelected = {
                            events(SurveyEvents.OnChangeAnswer(
                                index = index,
                                answer = it
                            ))
                        }
                    )
                    QuestionType.SHORT_ANSWER -> ShortAnswer(
                        question = question.question,
                        answer = question.answer,
                        onAnswerChanged = {
                            events(SurveyEvents.OnChangeAnswer(
                                index = index,
                                answer = it
                            ))
                        }
                    )
                    QuestionType.LONG_ANSWER -> LongAnswer(
                        question = question.question,
                        answer = question.answer,
                        onAnswerChanged = {
                            events(SurveyEvents.OnChangeAnswer(
                                index = index,
                                answer = it
                            ))
                        }
                    )
                    QuestionType.TRUE_FALSE -> TrueOrFalse(
                        question = question.question,
                        answer = question.answer.toBoolean(),
                        onAnswerChanged = {
                            events(SurveyEvents.OnChangeAnswer(
                                index = index,
                                answer = it.toString()
                            ))
                        }
                    )
                    else  -> ShortAnswer(
                        question = question.question,
                        answer = question.answer,
                        onAnswerChanged = {
                            events(SurveyEvents.OnChangeAnswer(
                                index = index,
                                answer = it
                            ))
                        }
                    )
                }
            }

            item {
                ImagePicker(
                    modifier = Modifier.fillMaxWidth(),
                    selectedImageUri = selectedImage,
                    onImageSelected = {
                        events(SurveyEvents.OnImageChange(it))
                    }
                )
            }

            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    enabled = !isLoading,
                    onClick = {
                        events(SurveyEvents.OnSubmit)
                    }
                ) {
                    Box(
                        modifier = Modifier

                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(text = "Submit")
                        }
                    }

                }
            }


        }
    }

}
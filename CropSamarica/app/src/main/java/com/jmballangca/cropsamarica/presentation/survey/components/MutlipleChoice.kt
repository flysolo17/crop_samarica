package com.jmballangca.cropsamarica.presentation.survey.components

import android.widget.RadioGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.domain.models.questions.Question
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme


@Composable
fun MultipleChoice(
    modifier: Modifier = Modifier,
    selectedAnswers : List<String>,
    question: Question,
    onAnswerSelected: (String) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = question.text ?: "",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            question.options?.let {
                it.forEachIndexed { index, string ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    ){
                        Checkbox(
                            checked = selectedAnswers.contains(string),
                            onCheckedChange = {
                                onAnswerSelected(string)
                            }
                        )
                        Text(
                            text = string,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MultipleChoicePreview() {
    CropSamaricaTheme {
        MultipleChoice(
            selectedAnswers = listOf("Red", "Green"),
            question = Question(
                text = "What is your favorite color?",
                options = listOf("Red", "Green", "Blue", "Yellow")
            ),
            onAnswerSelected = {}
        )
    }
}

@Composable
fun SingleChoice(
    modifier: Modifier = Modifier,
    selectedAnswer : String,
    question: Question,
    onAnswerSelected: (String) -> Unit
) {

    Card(
        modifier = modifier.fillMaxWidth()
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = question.text ?: "",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            question.options?.forEachIndexed { index, string ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                ){
                    RadioButton(
                        selected = selectedAnswer == string,
                        onClick = {
                            onAnswerSelected(string)
                        }
                    )
                    Text(
                        text = string,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
     
        }
    }
}


@Preview
@Composable
private fun SingleChoicePreview() {
    CropSamaricaTheme {
        SingleChoice(
            selectedAnswer = "Red",
            question = Question(
                text = "What is your favorite color?",
                options = listOf("Red", "Green", "Blue", "Yellow")
            ),
            onAnswerSelected = {}
        )
    }
}


@Composable
fun ShortAnswer(
    modifier: Modifier = Modifier,
    answer: String,
    question: Question,
    onAnswerChanged: (String) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = question.text ?: "",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = answer,
                onValueChange = onAnswerChanged,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = "Answer")
                }
            )
        }
    }
}


@Preview
@Composable
private fun ShortAnswerPreview() {
    CropSamaricaTheme {
        ShortAnswer(
            answer = "Red",
            question = Question(
                text = "What is your favorite color?"
            ),
            onAnswerChanged = {}
        )
    }
}


@Composable
fun LongAnswer(
    modifier: Modifier = Modifier,
    answer: String,
    question: Question,
    onAnswerChanged: (String) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = question.text ?: "",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = answer,
                onValueChange = onAnswerChanged,
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                label = {
                    Text(text = "Answer")
                }
            )
        }
    }
}


@Preview
@Composable
private fun LongAnswerPreview() {
    CropSamaricaTheme {
        LongAnswer(
            answer = "Red",
            question = Question(
                text = "What is your favorite color?"
            ),
            onAnswerChanged = {}
        )
    }
}


@Composable
fun TrueOrFalse(
    modifier: Modifier = Modifier,
    answer: Boolean,
    question: Question,
    onAnswerChanged: (Boolean) -> Unit
) {
    val options = listOf(false, true)
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = question.text ?: "",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            options.forEachIndexed { index, boolean ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                ){
                    RadioButton(
                        selected = answer == boolean,
                        onClick = {
                            onAnswerChanged(boolean)
                        }
                    )
                    Text(
                        text = boolean.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TrueOrFalsePreview() {
    CropSamaricaTheme {
        TrueOrFalse(
            answer = true,
            question = Question(
                text = "What is your favorite color?"
            ),
            onAnswerChanged = {}
        )

    }
    
}
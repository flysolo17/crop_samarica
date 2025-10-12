package com.jmballangca.cropsamarica.presentation.home.components
import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jmballangca.cropsamarica.core.utils.UIState
import com.jmballangca.cropsamarica.core.utils.showToast
import com.jmballangca.cropsamarica.data.models.task.Task
import com.jmballangca.cropsamarica.data.models.task.TaskStatus
import com.jmballangca.cropsamarica.data.models.task.getBackgroundColor
import com.jmballangca.cropsamarica.data.models.task.getTextColor
import com.jmballangca.cropsamarica.domain.utils.monthAndDay
import com.jmballangca.cropsamarica.domain.utils.toDateOnly
import com.jmballangca.cropsamarica.presentation.toCamelCase
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme
import java.util.Date


@Composable
fun TaskDialog(
    modifier: Modifier = Modifier,
    onStatusChange: (TaskStatus,result : (UIState<String>) -> Unit) -> Unit,
    onEdit: (task : Task,result: (UIState<String>) -> Unit) -> Unit,
    onDelete: (result: (UIState<String>) -> Unit) -> Unit,
    task: Task,
) {
    var expanded by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    val statuses = TaskStatus.entries
    val context = LocalContext.current
    if (expanded) {
        Dialog(
            onDismissRequest = {
                expanded = !expanded
            }
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = task.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(
                            modifier = Modifier.weight(1f)
                        )
                        if (loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    statuses.forEachIndexed { index, status ->

                        val current = remember {
                            status
                        }
                        Column {
                            Text(
                                text = current.title,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = MaterialTheme.colorScheme.outline
                                )
                            )

                            Card(
                                shape = MaterialTheme.shapes.extraLarge,
                                colors = CardDefaults.cardColors(
                                    containerColor = current.getBackgroundColor(),
                                    contentColor = current.getTextColor()
                                ),
                                onClick = {
                                    if  (status == task.status) {
                                        return@Card
                                    }
                                    onStatusChange(status) {
                                        when(it) {
                                            is UIState.Error -> {
                                                loading = false
                                                expanded = false
                                                context.showToast(it.message)
                                            }
                                            UIState.Loading -> {loading = true}
                                            is UIState.Success<String> -> {
                                                loading = false
                                                expanded = false
                                                context.showToast(it.data)
                                            }
                                        }
                                    }
                                }
                            ) {
                                Text(
                                    text = current.title,
                                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Button(
                            onClick = {
                                onDelete {
                                    when(it) {
                                        is UIState.Error -> {
                                            loading = false
                                            expanded = false
                                            context.showToast(it.message)
                                        }
                                        UIState.Loading -> {loading = true}
                                        is UIState.Success<String> -> {
                                            loading = false
                                            expanded = false
                                            context.showToast(it.data)
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            )
                        ) {
                            Text(stringResource(com.jmballangca.cropsamarica.R.string.delete))
                        }

                        EditTask(
                            modifier = Modifier.weight(1f),
                            task = task,
                            onClose = {
                                expanded = !expanded
                            }
                        ) { newTask ,result ->
                            onEdit(newTask,result)
                        }

                    }
                }
            }
        }
    }
    TaskCard(
        modifier = modifier,
        task = task,
        enableBorder = false,
        onClick = {
            expanded = !expanded
        }
    )
}

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    task: Task,
    enableBorder : Boolean = true,
    onClick: () -> Unit = {}
) {
    val isDone = task.status == TaskStatus.COMPLETED
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        onClick = {
            onClick()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 24.dp,
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                VerticalDivider(
                    thickness = 4.dp,
                    modifier = Modifier.height(24.dp),
                    color = task.status.getTextColor()
                )


                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                    )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp
                    ),
            ) {
                val date = task.getStartAndDue()

                date?.let {
                    Text(
                        modifier = Modifier
                            .background(
                                color = task.getBackgroundColor(),
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(
                                4.dp
                            ),
                        text = it,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color= task.getTextColor()
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (task.description.isNotEmpty()) {
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray,
                            textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
                        )
                    )
                }
            }


        }
    }

}
fun Task?.getStartAndDue(): String? {
    if (this == null) return null

    val start = this.startDate?.monthAndDay()
    val due = this.dueDate?.monthAndDay()
    return when {
        start == null && due == null -> null
        start == due -> start
        start == null -> due
        due == null -> start
        else -> "$start - $due"
    }
}
@Preview
@Composable
private fun TaskCardPrev() {
    CropSamaricaTheme {
        TaskCard(
            task = Task(
                id = "1",
                title = "Task 1",
                description = "Task 1 description",

                startDate = Date(),
                dueDate = Date(),
                status = TaskStatus.PENDING
            )
        )
    }
}
package com.jmballangca.cropsamarica.presentation.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.tasks.Tasks
import com.jmballangca.cropsamarica.core.ui.UnknownError
import com.jmballangca.cropsamarica.core.utils.shimmer
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.task.RiceFieldTask
import com.jmballangca.cropsamarica.data.models.task.Task

import com.jmballangca.cropsamarica.presentation.home.components.NoTaskFound
import com.jmballangca.cropsamarica.presentation.home.components.TaskCard
import com.jmballangca.cropsamarica.presentation.task.components.CreateTaskBottomSheet
import com.jmballangca.cropsamarica.presentation.task.components.ViewTaskBottomSheet

import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme
import kotlinx.coroutines.launch


@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    riceFields : List<RiceField>,
    viewModel: TaskViewModel = hiltViewModel()
) {

    val new by rememberUpdatedState(
        newValue = riceFields
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events

    LaunchedEffect(riceFields) {
        if (riceFields.isNotEmpty()) {
            events(TaskEvent.LoadTask(riceFields))
        }
    }

    val selectedTask = state.selectedTask
    if (selectedTask != null) {
        ViewTaskBottomSheet(
            task = selectedTask,
            onDismiss = {
                events(TaskEvent.OnTaskSelected(null))
            },
            onDelete = { id ->
                events(TaskEvent.OnDeleteTask(id))
            },
            onEdit = { task ->
                events(TaskEvent.OnUpdateTask(task))
            }
        )
    }
    if (riceFields.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            NoTaskFound {

            }
        }
    } else {
        TaskScreen(
            modifier = modifier,
            riceFields = riceFields,
            tasks = state.tasks,
            isLoading = state.isLoading,
            events = events
        )
    }
}
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    riceFields : List<RiceField>,
    tasks : List<Task>,
    isLoading : Boolean = false,

    events : (TaskEvent) -> Unit
) {
    val tasks = tasks

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        riceFields.size
    }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(
                        horizontal = 16.dp
                    )
                ,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "My Tasks", style = MaterialTheme.typography.titleMedium)
                CreateTaskBottomSheet(
                    riceFields = riceFields,
                    onSave = { task, result ->
                        events(TaskEvent.OnCreateTask(
                        task = task,
                            result= result
                    ))},
                    selectedRiceField = riceFields[pagerState.currentPage]
                )
            }
        }

        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 0.dp,

            ) {
                riceFields.forEachIndexed { index, rice ->
                    val selected = pagerState.currentPage == index
                    Tab(
                        selected = selected,

                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },

                        text = {
                            Text(
                                text = rice.name,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                        }
                    )
                }
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) {page ->
            val riceField = riceFields[page]
            val tasks = tasks.filter { it.fieldId == riceField.id && it.stage == riceField.stage}
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    NoTaskFound {

                    }
                }

            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(tasks) { task ->
                        TaskCard(task = task, onClick = {
                            events(TaskEvent.OnTaskSelected(task))
                        })
                    }
                }
            }

        }
    }


}

@Preview(
    showBackground = true,
)
@Composable
private fun TaskScrenPrev() {
    CropSamaricaTheme {
        val sampleRiceFields = listOf(
            RiceField(
                name = "Sample Rice Field",
            )
        )
        TaskScreen(
            riceFields = sampleRiceFields,
            tasks = emptyList(),
            isLoading = false,
            events = {},
        )
    }
    
}
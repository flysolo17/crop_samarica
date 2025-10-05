package com.jmballangca.cropsamarica.presentation.view_crop_field

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jmballangca.cropsamarica.core.ui.CollapsingToolbar
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.core.utils.Reminder
import com.jmballangca.cropsamarica.core.utils.showToast
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.data.models.rice_field.getIcon
import com.jmballangca.cropsamarica.data.models.task.Task
import com.jmballangca.cropsamarica.presentation.RiceFieldDateTimeConverter
import com.jmballangca.cropsamarica.presentation.common.LoadingScreen
import com.jmballangca.cropsamarica.presentation.create_crop_field.CreateCropFieldScreen
import com.jmballangca.cropsamarica.presentation.home.components.TaskCard
import com.jmballangca.cropsamarica.presentation.view_crop_field.components.DeleteCropDialog
import com.jmballangca.cropsamarica.presentation.view_crop_field.components.ViewAboutInfo
import com.jmballangca.cropsamarica.presentation.weather_forecast.ReminderCard
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ViewCropFieldScreen(
    modifier: Modifier = Modifier,
    id : String,
    navController: NavController,
    viewModel: ViewCropFieldViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    val oneTimeEvents = viewModel.oneTimeEvents
    LaunchedEffect(id) {
        id.isNotEmpty().let {
            events(ViewCropFieldEvents.GetRiceField(id))
        }
    }
    val context = LocalContext.current
    LaunchedEffect(oneTimeEvents) {
        oneTimeEvents.collect { event ->
            when (event) {
                is OneTimeEvents.Navigate -> {
                    navController.navigate(event.route)
                }
                OneTimeEvents.NavigateBack -> {
                    navController.popBackStack()
                }

                is OneTimeEvents.ShowToast -> {
                    context.showToast(event.message)

                }
            }
        }
    }
    ViewCropFieldScreen(
        modifier = modifier,
        riceField = state.riceField ?: RiceField(),
        isLoading = state.isLoading,
        onBack = {
            navController.popBackStack()
        },
        selectedStage = state.selectedTab,
        tasks = state.tasks,
        reminders = state.reminders,
        onStageSelected = {
            events(ViewCropFieldEvents.OnStageSelected(it))
        },
        onDeleteCrop = {
            events(ViewCropFieldEvents.OnDeleteCrop(it))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewCropFieldScreen(
    modifier: Modifier = Modifier,
    riceField: RiceField,
    isLoading: Boolean = false,
    tasks: List<Task> = emptyList(),
    reminders: List<Reminder> = emptyList(),
    onBack: () -> Unit = {},
    selectedStage: RiceStage,
    onDeleteCrop : (String) -> Unit,
    onStageSelected : (RiceStage) -> Unit = {},
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = riceField.name)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    DeleteCropDialog(
                        name = riceField.name,
                    ) {
                        onDeleteCrop(riceField.id)
                    }
                    ViewAboutInfo(
                        riceField = riceField
                    )
                }
            )

        }
    ) {
        if (isLoading) {
            LoadingScreen(
                modifier = modifier
                    .fillMaxSize()
                    .padding(it),
                title = "Loading..."
            )
        } else {
            val stages = RiceStage.entries
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(it),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),

            ) {
                val stages = RiceStage.entries
                itemsIndexed(
                    items = stages,
                    key = { _, stage -> stage.ordinal }
                ) { index, stage ->
                    StageItem(
                        selected = index == selectedStage.ordinal,
                        isLast = index == stages.size - 1,
                        stage = stage,
                        isCurrentStage = selectedStage == stage,
                        onClick = {
                            onStageSelected(stage)
                        }
                    )
                }
            }
        }


    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RiceFieldDetailScreen(riceField: RiceField) {
    val dateFormatter = remember {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    }

    val riceFieldDateTimeConverter = remember {
        RiceFieldDateTimeConverter(
            plantedDate = riceField.plantedDate,
            expectedHarvestDate = riceField.expectedHarvestDate
                ?: System.currentTimeMillis()
        )
    }
    val details = listOf(
        "Variety" to riceField.variety.name,
        "Stage" to riceField.stage.name,
        "Status" to riceField.status.name,
        "Area Size" to "${riceField.areaSize} hectares",
        "Location" to riceField.location,
        "Planted Date" to dateFormatter.format(Date(riceField.plantedDate)),
    ) + listOfNotNull(
        riceField.expectedHarvestDate?.let {
            "Expected Harvest" to dateFormatter.format(Date(it))
        },
        "Days" to "${riceFieldDateTimeConverter.displayDate()}"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = riceField.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            details.forEach { (label, value) ->
                DetailItem(label = label, value = value,modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun DetailItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Column(modifier = modifier.padding(8.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun StageItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    isLast : Boolean = false,
    isCurrentStage :Boolean = false,
    stage: RiceStage,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(
                if (!selected) 100.dp else Dp.Unspecified
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isCurrentStage) {
                Image(
                    painter = painterResource(stage.getIcon()),
                    contentDescription = stage.name,
                    modifier = Modifier.size(48.dp).clickable {
                        onClick()
                    },
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    painter = painterResource(stage.getIcon()),
                    contentDescription = stage.name,
                    modifier = Modifier.size(48.dp).clickable {
                        onClick()
                    },
                )
            }
            if (!isLast) {
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight(),
                    color = if (isCurrentStage) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stage.displayName,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = if (isCurrentStage) MaterialTheme.colorScheme.primary else Color.Unspecified
                ),
            )
            Text("${stage.daysRange.first} - ${stage.daysRange.last} days", style = MaterialTheme.typography.bodySmall)
            if (selected) {
                Text(
                    text = stage.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray,
                    ),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Icon(
                        imageVector = Icons.Default.CheckBox,
                        contentDescription = "Check",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text("Things you should do", style = MaterialTheme.typography.titleSmall)
                }
                stage.thingsToDo.forEach {
                    Text(
                        text = "- $it",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray
                        ),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Check",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )
                    Text("Things to look out for", style = MaterialTheme.typography.titleSmall)
                }
                stage.thingsToLookOutFor.forEach {
                    Text(
                        text = "- $it",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray
                            ),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }


        }
    }

}

@Preview(
    showBackground = true
)
@Composable
private fun StageItemPrev() {
    CropSamaricaTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val stages = RiceStage.entries
            stages.forEachIndexed { index, stage ->
                StageItem(
                    selected = index == 2,
                    isLast = index == stages.size - 1,
                    stage = stage,
                    isCurrentStage = index == 2
                )
            }
        }

    }
}

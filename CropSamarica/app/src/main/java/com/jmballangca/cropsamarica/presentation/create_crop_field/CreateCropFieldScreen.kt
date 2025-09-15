package com.jmballangca.cropsamarica.presentation.create_crop_field

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jmballangca.cropsamarica.core.ui.CollapsingToolbar
import com.jmballangca.cropsamarica.core.ui.IrrigationSelector
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.core.utils.showToast
import com.jmballangca.cropsamarica.data.models.rice_field.IrrigationType
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.presentation.common.DatePickerModal
import com.jmballangca.cropsamarica.presentation.common.RiceStagePicker
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.FullScreenLoading
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.ImagePicker
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.RecommendationDialog
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.RiceVariety
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.RiceVarietySelector
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.SoilTypeSelector
import com.jmballangca.cropsamarica.presentation.create_crop_field.components.SoilTypes
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme
import kotlinx.coroutines.delay
import java.util.Date

data class CreateCropFieldForm(
    val name: String = "",
    val location: String = "",
    val soilType: String = "",
    val area: String = "",
    val plantedDate: String = "",
    val variety: String = "",
    val lastFertilizer: String = "",
    val irrigation: String = ""
)

@Composable
fun CreateCropFieldScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: CreateCropFieldViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    val oneTimeEvents = viewModel.oneTimeEvents
    val context = LocalContext.current
    LaunchedEffect(oneTimeEvents) {
        oneTimeEvents.collect {
            when (it) {
                is OneTimeEvents.Navigate -> {
                    navController.navigate(it.route)
                }
                OneTimeEvents.NavigateBack -> {
                    navController.popBackStack()
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
                navController.popBackStack()
            },
            onCreateTask = {
                events(CreateCropFieldEvents.OnCreateTask(it))
            }
        )
    }
    when {
        state.isLoading -> {
            FullScreenLoading(
                title = "Generating recommendations..."
            )
        }
        state.isCreatingTask -> {
            FullScreenLoading(
                title = "Saving Task..."
            )
        }
        else -> {
            CreateCropFieldScreen(
                modifier = modifier,
                navController = navController,
                isLoading = state.isLoading,
                name = state.name,
                location = state.location,
                soilType = state.soilType,
                area = state.areaSize,
                plantedDate = state.plantedDate,
                variety = state.variety,
                irrigation = state.irrigationType,
                selectedImageUri = state.imageUri,
                events = events,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCropFieldScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    isLoading: Boolean = false,
    name : String,
    location : String,
    soilType : SoilTypes?,
    area : String,
    plantedDate : Date?,
    variety : RiceVariety?,
    selectedImageUri : Uri?,
    irrigation : IrrigationType,
    events: (CreateCropFieldEvents) -> Unit,
) {

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CollapsingToolbar(
                scrollBehavior = scrollBehavior,
                onBack = { navController.popBackStack() }
            ) {
                if (scrollBehavior.state.collapsedFraction < 0.5f) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Create Crop Field",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            "Enter accurate details to receive better recommendations on crop management, soil health, and harvest planning.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                } else {
                    Text(
                        text = "Create Crop Field",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    enabled = !isLoading,
                    onClick = {
                        events(CreateCropFieldEvents.Submit)
                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(6.dp)
                    ){
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(20.dp)
                            )
                        } else {
                            Text(
                                text = "Submit",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                }
            }
        }
    ) { innerPadding ->
        val color = TextFieldDefaults.colors()
        val shape = MaterialTheme.shapes.medium

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    onValueChange = {
                        events(CreateCropFieldEvents.CropForm.Name(it))
                    },
                    label = { Text("Rice field name *") },
                    shape = shape,
                    colors = color,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = androidx.compose.ui.text.input.ImeAction.Next
                    ),
                    singleLine = true
                )
            }
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = location,
                    onValueChange = {
                        events(CreateCropFieldEvents.CropForm.Location(it))
                    },
                    label = { Text("Location *") },
                    shape = shape,
                    colors = color,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = androidx.compose.ui.text.input.ImeAction.Next
                    ),
                    singleLine = true
                )
            }
            item(
                span = { GridItemSpan(1) }
            ) {
                RiceStagePicker(
                    selected = plantedDate,
                    onSelected = {
                        events(CreateCropFieldEvents.CropForm.PlantedDate(it))
                    }
                )
            }
            item(
                span = { GridItemSpan(1) }
            ) {

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = area,
                    onValueChange = {
                        events(CreateCropFieldEvents.CropForm.Area(it))
                    },
                    label = { Text("Area size *") },
                    shape = shape,
                    colors = color,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = androidx.compose.ui.text.input.ImeAction.Next
                    )
                )
            }
            item(
                span = { GridItemSpan(2) }
            ){
                RiceVarietySelector(
                    selected = variety,
                    onSelected = {
                        events(CreateCropFieldEvents.CropForm.Variety(it))
                    }
                )
            }
            item(
                span = { GridItemSpan(2) }
            ){
                SoilTypeSelector(
                    selected = soilType,
                    onSelected = {
                        events(CreateCropFieldEvents.CropForm.SoilType(it))
                    }
                )
            }
            item(
                span = { GridItemSpan(2) }
            ){
                IrrigationSelector(
                    selected = irrigation,
                    onSelected = {
                        events(CreateCropFieldEvents.CropForm.Irrigation(it))
                    }
                )
            }
            item(
                span = { GridItemSpan(2) }) {
                ImagePicker(
                    selectedImageUri = selectedImageUri,
                    onImageSelected = {
                        events(CreateCropFieldEvents.CropForm.OnImageSelected(it))
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun CreateCropFieldNamePrev() {
    CropSamaricaTheme {
        CreateCropFieldScreen(
            navController = rememberNavController(),
            isLoading = false,
            name = "",
            location = "",
            soilType = null,
            area = "",
            plantedDate = null,
            variety = null,
            irrigation = IrrigationType.GRAVITY_IRRIGATION,
            selectedImageUri = null,
            events = {}
        )
    }
}
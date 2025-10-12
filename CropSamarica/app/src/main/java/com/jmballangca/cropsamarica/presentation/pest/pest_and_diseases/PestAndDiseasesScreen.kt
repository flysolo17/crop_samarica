package com.jmballangca.cropsamarica.presentation.pest.pest_and_diseases

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jmballangca.cropsamarica.MainActivity
import com.jmballangca.cropsamarica.core.utils.shimmer
import com.jmballangca.cropsamarica.data.models.pest.GOLDEN_APPLE_SNAIL
import com.jmballangca.cropsamarica.data.models.pest.PestAndDisease
import com.jmballangca.cropsamarica.data.models.pest.locale
import com.jmballangca.cropsamarica.data.models.rice_field.RiceStage
import com.jmballangca.cropsamarica.data.models.rice_field.getIcon
import com.jmballangca.cropsamarica.presentation.navigation.PEST_AND_DISEASES_DETAIL
import com.jmballangca.cropsamarica.restartApp
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme


@Composable
fun PestAndDiseasesScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: PestAndDiseasesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    PestAndDiseasesScreen(
        modifier = modifier,
        language = state.language,
        isLoading = state.isLoading,
        pestAndDiseases = state.pestAndDiseases,
        onPestClicked = {
            navController.navigate(
                PEST_AND_DISEASES_DETAIL(it, state.language)
            )
        },
        onLanguageChanged = {
            viewModel.events(
                PestAndDiseaseEvents.OnLanguageChanged(it)
            )


        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PestAndDiseasesScreen(
    modifier: Modifier = Modifier,
    isLoading : Boolean = false,
    language : String,
    pestAndDiseases : List<PestAndDisease> = emptyList(),
    onPestClicked : (String) -> Unit,
    onLanguageChanged : (String) -> Unit = {}
) {

    val data= if (isLoading) PestAndDisease.ALL else pestAndDiseases
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
            ) {
                Text(text = "Pest And Diseases -${language}", style = MaterialTheme.typography.titleMedium)
                IconButton(
                    onClick = {
                        if (language == "en") {
                            onLanguageChanged("tl")
                        } else {
                            onLanguageChanged("en")
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Language"
                    )
                }
            }
        }

        val stages = RiceStage.entries
        items(stages) { stage ->
            val filteredPestAndDiseases = data.filter { it.stages.contains(stage) }
            if (filteredPestAndDiseases.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                ) {

                    Row(
                        modifier = Modifier.padding(
                            bottom = 8.dp
                        ),
                        verticalAlignment = androidx.compose.ui.Alignment.Bottom,
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                    ){
                        Image(
                            painter = painterResource(id = stage.getIcon()),
                            contentDescription = stage.name,
                            modifier = Modifier.size(32.dp).shimmer(shimmering = isLoading)
                        )
                        Text(text = stage.name,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.shimmer(shimmering = isLoading)
                        )
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(
                                state = androidx.compose.foundation.rememberScrollState()
                            )
                        ,
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                    ) {
                        filteredPestAndDiseases.forEach {
                            PestAndDiseaseItem(
                                pestAndDisease = it,
                                isLoading = isLoading,
                                language = language
                            ) {
                                onPestClicked(it.id)
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun PestAndDiseaseItem(
    modifier: Modifier = Modifier,
    pestAndDisease: PestAndDisease,
    isLoading: Boolean = false,
    language: String,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier.size(220.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ){
            AsyncImage(
                model = pestAndDisease.images.first(),
                placeholder = painterResource(
                    id = com.jmballangca.cropsamarica.R.drawable.profile_bg
                ),
                error = painterResource(
                    id = com.jmballangca.cropsamarica.R.drawable.profile_bg
                ),
                modifier = Modifier.weight(1f).shimmer(shimmering = isLoading,shape = MaterialTheme.shapes.medium),
                contentDescription = pestAndDisease.title.locale(
                    language
                ),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                val firstWord = pestAndDisease.title.locale(
                    language
                ).substringBefore("–").trim()
                val lastWord = pestAndDisease.title.locale(
                    language
                ).substringAfter("–").trim()

                Text(text = firstWord, style = MaterialTheme.typography.titleMedium, modifier = Modifier.shimmer(shimmering = isLoading))
                Text(text = lastWord, style = MaterialTheme.typography.titleSmall.copy(

                    color= MaterialTheme.colorScheme.outline
                ) ,maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.shimmer(shimmering = isLoading)
                )
            }
        }


    }
   
}

@Preview
@Composable
private fun PestAndDiseaseItemPrev() {
    CropSamaricaTheme {
        PestAndDiseaseItem(
            pestAndDisease = GOLDEN_APPLE_SNAIL,
            onClick = {},
            language = "en"
        )
    }
}
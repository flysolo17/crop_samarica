package com.jmballangca.cropsamarica.presentation.developers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jmballangca.cropsamarica.domain.models.Developers
import com.jmballangca.cropsamarica.presentation.home.components.ProfileImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperScreen(
    modifier: Modifier = Modifier,
    primaryNavController : NavController,
    viewModel: DeveloperViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DeveloperScreen(
        isLoading = state.isLoading,
        developers = state.developers,
        error = state.error,
        onBackClick = {
            primaryNavController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    developers: List<Developers> = emptyList(),
    error: String? = null,
    onBackClick: () -> Unit
) {
    val nestedScrollConnection = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(nestedScrollConnection.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                scrollBehavior = nestedScrollConnection,
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                expandedHeight = 220.dp,
                title = {
                    if (nestedScrollConnection.state.collapsedFraction == 1f) {
                        Text(text = "Developers")
                    } else {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Developers")
                        }
                    }

                }
            )

        }
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(it),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            if (isLoading) {
                item {
                    LinearProgressIndicator()
                }
            }
            if (error != null) {
                item {
                    Text(text = error)
                }
            }
            items(developers.size) { index ->
                val developer = developers[index]
                DeveloperItem(developer = developer)
            }
        }

    }

}

@Composable
fun DeveloperItem(
    modifier: Modifier = Modifier,
    developer: Developers
) {
    OutlinedCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileImage(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                profile = developer.profile,
                name = developer.name,
                imageSize = 100.dp
            )
            Text(
                text = developer.name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = developer.email,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.outline
                )
            )
            Text(
                text = developer.roles.joinToString { it }.replace(","," / "),
                style = MaterialTheme.typography.titleSmall
            )
        }

    }
}
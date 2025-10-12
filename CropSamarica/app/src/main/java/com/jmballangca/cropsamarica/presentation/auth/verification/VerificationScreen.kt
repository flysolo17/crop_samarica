package com.jmballangca.cropsamarica.presentation.auth.verification

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jmballangca.cropsamarica.R
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.core.utils.showToast
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme

@Composable
fun VerificationScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: VerificationViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events = viewModel::events
    val oneTimeEvents = viewModel.oneTimeEvents
    val  context = LocalContext.current
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
    VerificationScreen(
        isVerified = state.isVerified,
        isLoading = state.isLoading,
        timer = state.timer,
        modifier = modifier,
        events = events
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(
    modifier: Modifier = Modifier,
    isVerified: Boolean = false,
    isLoading: Boolean = false,
    timer: Long = 0L,
    events: (VerificationEvents) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                },
                navigationIcon = {

                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                Button(
                    enabled = !isLoading,
                    onClick = {
                        events(VerificationEvents.SendEmailVerification)
                    },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Text(
                        text = if (isLoading) "$timer seconds.." else "Send Email Verification",
                        modifier = Modifier.padding(8.dp)
                    )

                }
            }
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = R.drawable.verification), contentDescription = "Verification")
            Spacer(modifier = modifier.height(12.dp))
            Text(text = "Verify your email address", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = modifier.height(8.dp))
            Text(
                text = "To use Crop Samarica app, You need to verify your email address.",
                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray),
                textAlign = TextAlign.Center,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun VerificationScreenPrev() {
    CropSamaricaTheme {
        VerificationScreen()
    }
}
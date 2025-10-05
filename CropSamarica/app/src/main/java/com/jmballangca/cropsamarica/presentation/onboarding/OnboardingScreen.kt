package com.jmballangca.cropsamarica.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.R
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onSkip : () -> Unit = {},
    onStart : () -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        3
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {

                },
                actions = {
                    TextButton(
                        onClick = onSkip,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text("Skip")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(pagerState.pageCount) {
                        val selected =  pagerState.currentPage == it
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(
                                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                                    shape = CircleShape
                                )
                        ) {

                        }
                    }
                }

            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) {
                when (it) {
                    0 -> WelcomePage {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                    1 -> CropManagement(
                        onBack = {
                            scope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        },
                        onNext = {
                            scope.launch {
                                pagerState.animateScrollToPage(2)
                            }
                        }
                    )
                    2 -> AiAssistantPage(
                        onBack = {
                            scope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        },
                        onNext = onStart
                    )
                }
            }


        }
    }
}

@Composable
fun CropManagement(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onNext: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.crop_management),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
        )
        Text(
            "Manage your Rice Field anywhere in the world.",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            ),
        )

        Spacer(
            modifier = Modifier.size(16.dp)
        )

        Text(
            "With our app you can:\n" +
                    "• Track your rice growth stages\n" +
                    "• Receive AI-based farming recommendations\n" +
                    "• Monitor field updates in real time\n" +
                    "• Improve yield with data-driven insights",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(
            modifier = Modifier.size(24.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onBack,

                shape = MaterialTheme.shapes.small
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
                Spacer(
                    modifier = Modifier.size(8.dp)
                )
                Text("Back")
            }
            Button(
                onClick = onNext,
                shape = MaterialTheme.shapes.small
            ) {
                Text("Next")
                Spacer(
                    modifier = Modifier.size(8.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next"
                )
            }
        }
    }
}

@Composable
fun WelcomePage(
    modifier: Modifier = Modifier,
    start : () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
        )
        Text(
            "Your Digital Kaagapay sa Palayan",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            ),
        )

        Spacer(
            modifier = Modifier.size(16.dp)
        )
        Text("Monitor your rice crops with precision. Track growth stages, optimize yields, and make smarter farming decisions.",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
        ))

        Spacer(
            modifier = Modifier.size(24.dp)
        )

        Button(
            onClick = start,
            shape = MaterialTheme.shapes.small
        ) {
            Text("Get Started")
            Spacer(
                modifier = Modifier.size(8.dp)
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Start"
            )
        }
    }
    
}

@Composable
fun AiAssistantPage(modifier: Modifier = Modifier,
onBack: () -> Unit,
onNext : () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.task_management),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
        )

        Text(
            "Plan and Track Your Farm Tasks",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            ),
        )

        Spacer(
            modifier = Modifier.size(16.dp)
        )
        Text("Stay organized by scheduling watering, fertilizing, and pest control tasks. Get reminders and monitor your farm’s daily activities efficiently.",
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center)
        )
        Spacer(
            modifier = Modifier.size(24.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onBack,

                shape = MaterialTheme.shapes.small
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
                Spacer(
                    modifier = Modifier.size(8.dp)
                )
                Text("Back")
            }
            Button(
                onClick = onNext,
                shape = MaterialTheme.shapes.small
            ) {
                Text("Next")
                Spacer(
                    modifier = Modifier.size(8.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next"
                )
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPrev() {
    CropSamaricaTheme {
        OnboardingScreen(
            onSkip = {},
            onStart = {}
        )

    }

}


@Preview(
    showBackground = true
)
@Composable
private fun WelcomPagePrev() {
    CropSamaricaTheme {
        WelcomePage {  }
    }

}

@Preview(
    showBackground = true
)
@Composable
private fun AiAssistantPagePrev() {
    CropSamaricaTheme {
        AiAssistantPage(
            onBack = {  },
            onNext = {  }
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun CropmanagementPrev() {
    CropSamaricaTheme {
        CropManagement(
            onBack = {  },
            onNext = {  }
        )
    }
}
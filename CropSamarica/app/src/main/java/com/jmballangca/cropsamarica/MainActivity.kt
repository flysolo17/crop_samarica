package com.jmballangca.cropsamarica

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.jmballangca.cropsamarica.domain.models.questions.Question
import com.jmballangca.cropsamarica.presentation.auth.AuthScreen
import com.jmballangca.cropsamarica.presentation.chat.ChatScreen
import com.jmballangca.cropsamarica.presentation.create_crop_field.CreateCropFieldScreen
import com.jmballangca.cropsamarica.presentation.main.MainScreen
import com.jmballangca.cropsamarica.presentation.navigation.AUTH
import com.jmballangca.cropsamarica.presentation.navigation.CHAT
import com.jmballangca.cropsamarica.presentation.navigation.CREATE_CROP_FIELD
import com.jmballangca.cropsamarica.presentation.navigation.MAIN
import com.jmballangca.cropsamarica.presentation.navigation.ONBOARDING
import com.jmballangca.cropsamarica.presentation.navigation.PROFILE
import com.jmballangca.cropsamarica.presentation.navigation.SURVEY
import com.jmballangca.cropsamarica.presentation.onboarding.OnboardingScreen
import com.jmballangca.cropsamarica.presentation.profile.ProfileScreen
import com.jmballangca.cropsamarica.presentation.survey.SurveyScreen

import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        CropSamaricaTheme {
            val viewModel = hiltViewModel<MainViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else -> {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = if (state.hasUser) MAIN else ONBOARDING
                    ) {
                        composable<ONBOARDING> {
                            OnboardingScreen(
                                onSkip = {
                                    navController.navigate(AUTH)
                                },
                                onStart = {
                                    navController.navigate(AUTH)
                                }
                            )
                        }
                        composable<PROFILE> {
                            ProfileScreen(
                                primaryNavController = navController,
                                navController = navController
                            )
                        }
                        composable<AUTH> {
                            AuthScreen(
                                navController = navController
                            )
                        }
                        composable<MAIN> {
                            MainScreen(
                                primaryNavController = navController,
                            )
                        }
                        composable<CREATE_CROP_FIELD>(
                            enterTransition = {
                                slideInVertically(
                                    initialOffsetY = { it }, // start from bottom
                                    animationSpec = tween(
                                        durationMillis = 550,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                            },
                            exitTransition = {
                                slideOutVertically(
                                    targetOffsetY = { it }, // slide down
                                    animationSpec = tween(
                                        durationMillis = 550,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                            },
                            popEnterTransition = {
                                slideInVertically(
                                    initialOffsetY = { it },
                                    animationSpec = tween(
                                        durationMillis = 550,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                            },
                            popExitTransition = {
                                slideOutVertically(
                                    targetOffsetY = { it },
                                    animationSpec = tween(
                                        durationMillis = 550,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                            }
                        ) {
                            CreateCropFieldScreen(navController = navController)
                        }

                        composable<CHAT> {
                            ChatScreen(
                                primaryNavController = navController
                            )
                        }
                        composable<SURVEY> { backStackEntry ->
                            val survey = backStackEntry.toRoute<SURVEY>()

                            SurveyScreen(
                                id = survey.id,
                                navHostController = navController
                            )
                        }
                    }
                }
            }
        }
        }
    }
}


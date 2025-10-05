package com.jmballangca.cropsamarica

import android.Manifest
import android.app.AlertDialog

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.jmballangca.cropsamarica.domain.models.questions.Question
import com.jmballangca.cropsamarica.presentation.auth.AuthScreen

import com.jmballangca.cropsamarica.presentation.main.MainScreen
import com.jmballangca.cropsamarica.presentation.navigation.AUTH

import com.jmballangca.cropsamarica.presentation.navigation.MAIN
import com.jmballangca.cropsamarica.presentation.navigation.ONBOARDING
import com.jmballangca.cropsamarica.presentation.navigation.SURVEY

import com.jmballangca.cropsamarica.presentation.onboarding.OnboardingScreen
import com.jmballangca.cropsamarica.presentation.survey.SurveyScreen


import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import java.util.Calendar
import java.util.Date

fun Date.startTime(): Date {
    return Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time
}
fun Date.endTime(): Date {
    return Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }.time
}
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setupNotificationPermissionLauncher()
        setContent {
            CropSamaricaTheme {
                val viewModel = hiltViewModel<MainViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
        
                LaunchedEffect(Unit) {
                    askNotificationPermission()
                }

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
                        Main(state = state)
                    }
                }
            }
            generateToken()
        }
    }

    private fun setupNotificationPermissionLauncher() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->

        }
    }
    private fun generateToken() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FirebaseMessaging.getInstance().subscribeToTopic(userId)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FCM_TOKEN_LAUNCHER", "Subscribed to topic: $userId")
                    } else {
                        Log.e("FCM_TOKEN_LAUNCHER", "Subscription failed", task.exception)
                    }
                }
        } else {
            Log.w("FCM_TOKEN_LAUNCHER", "Permission denied or user is null")
        }
    }


    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    retrieveToken()
                }

                shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) -> {
                    showPermissionRationaleDialog()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            retrieveToken()
        }
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission required")
            .setMessage("This app uses notifications to alert you about important updates. Please allow notifications to stay informed.")
            .setPositiveButton("Allow") { _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun retrieveToken() {
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                Log.d("FCM_TOKEN_LAUNCHER", token)
            }
            .addOnFailureListener { e ->
                Log.e("FCM_TOKEN_LAUNCHER", "Failed to retrieve token", e)
            }
    }
}
@Composable
fun Main(
    modifier: Modifier = Modifier,
    state: MainState = MainState(),
) {
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
        composable<SURVEY> { backStackEntry ->
            val survey = backStackEntry.toRoute<SURVEY>()
            SurveyScreen(
                id = survey.id,
                navHostController = navController
            )
        }
    }
}



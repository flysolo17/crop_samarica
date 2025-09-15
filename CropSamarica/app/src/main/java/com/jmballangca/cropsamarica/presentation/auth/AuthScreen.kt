package com.jmballangca.cropsamarica.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jmballangca.cropsamarica.R
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.core.utils.showToast
import com.jmballangca.cropsamarica.presentation.auth.components.AuthTab
import com.jmballangca.cropsamarica.presentation.auth.components.LoginPage
import com.jmballangca.cropsamarica.presentation.auth.components.RegisterPage
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
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

                is OneTimeEvents.ShowToast -> {
                    context.showToast(it.message)
                }

                OneTimeEvents.NavigateBack -> {}
            }
        }
    }
    AuthScreen(
        modifier = modifier,
        isLoading = state.isLoading,
        isRegistering = state.isRegistering,
        isSubmitting = state.isSubmitting,
        onForgotPassword = { email ->
            events(AuthEvents.ForgotPassword(email))
        },
        onLogin = { email, password ->
            events(AuthEvents.Login(email, password))
        },
        onRegister = { name, email, password ->
            events(AuthEvents.Register(name, email, password))
        },
        onGoogleSignIn = {
            events(AuthEvents.SignWithGoogle)
        },
        onGoogleRegister = {
            events(AuthEvents.RegisterWithGoogle)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    isSubmitting: Boolean,
    isLoading: Boolean = false,
    isRegistering: Boolean = false,
    onForgotPassword: (email: String) -> Unit,
    onLogin: (email: String, password: String) -> Unit = { _, _ -> },
    onRegister: (name: String, email: String, password: String) -> Unit = { _, _, _ -> },
    onGoogleSignIn: () -> Unit = {},
    onGoogleRegister: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            LargeTopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Image(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(400.dp)
                                .padding(16.dp),
                            contentScale = ContentScale.FillHeight
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary

                )
            )
        }
    ) {
        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) {
            2
        }
        val scope = rememberCoroutineScope()
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {

            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp
                        )
                    ),
                color = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    AuthTab(
                        currentPage = pagerState.currentPage,
                        selectedPage = pagerState.currentPage,
                        onTabSelected = { index ->
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize(),
                        userScrollEnabled = false
                    ) {
                        when (it) {
                            0 -> LoginPage(
                                isSigningWithGoogle = isLoading,
                                onGoogleSignIn = onGoogleSignIn,
                                isLoading = isLoading,
                                onLogin = { email, password ->
                                    onLogin(email, password)
                                },
                                isSendingPasswordResetEmail = isSubmitting,
                                onForgotPassword = onForgotPassword
                            )
                            1 -> RegisterPage(
                                isLoading = isRegistering,
                                onGoogleRegister = onGoogleRegister,
                                onRegister = { name, email, password ->
                                    onRegister(name, email, password)
                                    scope.launch {
                                        pagerState.animateScrollToPage(0)
                                    }
                                }
                            )
                        }
                    }

                }
            }

        }

    }
}





@Preview
@Composable
private fun AuthScreenPrev() {
    CropSamaricaTheme {
        AuthScreen(
            isLoading = false,
            isRegistering = false,
            isSubmitting = false,
            onForgotPassword = {},
            onLogin = { _, _ -> },
            onRegister = { _, _, _ -> }
        )
    }
}
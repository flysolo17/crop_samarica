package com.jmballangca.cropsamarica.presentation.profile

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jmballangca.cropsamarica.R
import com.jmballangca.cropsamarica.core.ui.UnknownError
import com.jmballangca.cropsamarica.core.utils.OneTimeEvents
import com.jmballangca.cropsamarica.core.utils.showToast
import com.jmballangca.cropsamarica.data.models.user.User
import com.jmballangca.cropsamarica.presentation.common.LoadingScreen
import com.jmballangca.cropsamarica.presentation.home.components.ProfileImage
import com.jmballangca.cropsamarica.presentation.navigation.ONBOARDING
import androidx.core.graphics.scale
import coil.compose.AsyncImage
import com.jmballangca.cropsamarica.presentation.navigation.AUTH
import com.jmballangca.cropsamarica.presentation.navigation.DEVELOPERS
import com.jmballangca.cropsamarica.presentation.navigation.SETTINGS
import com.jmballangca.cropsamarica.presentation.navigation.USER_GUIDE
import com.jmballangca.cropsamarica.presentation.profile.components.ChangePassword
import com.jmballangca.cropsamarica.presentation.profile.components.EditProfile
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    primaryNavController: NavController,
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit = {}
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val oneTimeEvents = viewModel.oneTimeEvents
    val events = viewModel::events
    val context = LocalContext.current
    LaunchedEffect(key1 = oneTimeEvents) {
        oneTimeEvents.collect {
            when (it) {
                is OneTimeEvents.Navigate -> {

                }
                OneTimeEvents.NavigateBack -> {
                    navController.popBackStack()
                }
                is OneTimeEvents.ShowToast -> {
                    if (it.message == "Logout Successful") {
                        onLogout()
                    }
                    context.showToast(it.message)
                }
            }
        }
    }
    when {
        state.isLoading -> {
            LoadingScreen(
                title = stringResource(R.string.getting_user_info)
            )
        }
        !state.isLoading && state.user != null -> {
            ProfileScreen(
                onBack = {
                    primaryNavController.popBackStack()
                },
                modifier = modifier,
                user = state.user!!,
                events = events,
                onLogout = {
                    events(ProfileEvents.OnLogout)
                },
                navController = navController
            )
        }
        else -> {
            UnknownError(
                onBack = {
                    navController.popBackStack()
                },
                message = stringResource(R.string.no_user_found)
            )
        }

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    user: User,
    onLogout: () -> Unit,
    onBack : () -> Unit,
    events: (ProfileEvents) -> Unit,
    navController: NavController,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            val height = 250.dp
            val topBarImageBackground = painterResource(R.drawable.profile_bg)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .background(Color.Transparent)
                    .clip(
                        shape = RoundedCornerShape(
                            bottomStart = 32.dp,
                            bottomEnd = 32.dp
                        )
                    )
                ,
            ) {
                Image(
                    painter = topBarImageBackground,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Color.Black.copy(
                                alpha = 0.5f
                            )
                        )
                ) {

                }


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                        .padding(
                            bottom = 16.dp
                        )
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {

                    IconButton(
                        modifier = Modifier.align(Alignment.Start),
                        onClick = {
                            onBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    ProfileImage(
                        name = user.name,
                        profile = user.profile,
                        imageSize = 56.dp,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = user.name.uppercase(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.White.copy(
                                alpha = 0.7f
                            )
                        )
                    )
                }
            }

        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = stringResource(R.string.user_menu),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.align(Alignment.Start)
            )
            EditProfile(
                currentName = user.name,
                onSaveChanges = { name, result ->
                    events(ProfileEvents.OnChangeName(name, result))
                }
            )
            ChangePassword {
                currentPassword, newPassword, result ->
                events(ProfileEvents.OnChangePassword(currentPassword, newPassword, result))
            }
            Text(
                text = stringResource(R.string.others),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.align(Alignment.Start)
            )
            ProfileButtons(
                icon = Icons.Filled.Book,
                title = stringResource(R.string.user_guide),
                onClick = {
                    navController.navigate(USER_GUIDE)
                }
            )
            ProfileButtons(
                icon = Icons.Filled.Code,
                title = stringResource(R.string.developers),
                onClick = {
                    navController.navigate(DEVELOPERS)
                }
            )

            ProfileButtons(
                icon = Icons.Filled.Settings,
                title = stringResource(R.string.settings),
                onClick = {
                    navController.navigate(SETTINGS)
                }
            )
            Button(
                onClick = onLogout,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(text = stringResource(R.string.logout), modifier = Modifier.padding(8.dp))
            }
            Text("Crop Samarica 2025", style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.outline
            ))
        }
    }
}


@Composable
fun ProfileButtons(
    modifier: Modifier = Modifier,
    icon : ImageVector,
    title : String,
    onClick : () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.ArrowForwardIos,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.outline
            )
        }

    }
}
@Preview
@Composable
private fun ProfileScreenPrev() {
    CropSamaricaTheme {
        ProfileScreen(
            user = User(
                id = "",
                name = "Juan Dela Cruz",
                email = "john.c.breckinridge@altostrat.com",
                profile = ""
            ),
            onBack = {},
            onLogout = {},
            events = {},
            navController = NavController(LocalContext.current)
        )
    }

}
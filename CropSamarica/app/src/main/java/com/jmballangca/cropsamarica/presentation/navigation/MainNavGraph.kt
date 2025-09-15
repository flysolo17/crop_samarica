package com.jmballangca.cropsamarica.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.user.User
import com.jmballangca.cropsamarica.presentation.chat.ChatScreen
import com.jmballangca.cropsamarica.presentation.home.HomeScreen
import com.jmballangca.cropsamarica.presentation.profile.ProfileScreen
import com.jmballangca.cropsamarica.presentation.report.ReportScreen
import com.jmballangca.cropsamarica.presentation.settings.SettingsScreen
import com.jmballangca.cropsamarica.presentation.task.TaskScreen


@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    primaryNavController: NavHostController,
    navController: NavHostController,
    user: User,
    onExpand : () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = HOME(),
        modifier = modifier
    ) {
        composable<HOME> {
            val id = it.arguments?.getString("id")

            HomeScreen(
                navController = navController,
                id = id,
                user = user,
                primaryNavController = primaryNavController,
                onCreateCropField = {
                    primaryNavController.navigate(CREATE_CROP_FIELD)
                },
                onExpand = {
                    onExpand()
                },
                onProfileSelected = {
                    primaryNavController.navigate(PROFILE)
                }
            )
        }
        composable<CHAT> {
            ChatScreen()
        }
        composable<TASK> {
            TaskScreen()
        }


    }

}
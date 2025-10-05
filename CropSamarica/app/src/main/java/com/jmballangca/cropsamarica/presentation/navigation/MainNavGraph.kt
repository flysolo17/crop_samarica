package com.jmballangca.cropsamarica.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.user.User
import com.jmballangca.cropsamarica.domain.models.Notifications

import com.jmballangca.cropsamarica.presentation.create_crop_field.CreateCropFieldScreen
import com.jmballangca.cropsamarica.presentation.developers.DeveloperScreen
import com.jmballangca.cropsamarica.presentation.home.HomeScreen
import com.jmballangca.cropsamarica.presentation.notifications.NotificationScreen
import com.jmballangca.cropsamarica.presentation.notifications.view.ViewNotificationScreen
import com.jmballangca.cropsamarica.presentation.pest.details.PestAndDiseaseDetailScreen
import com.jmballangca.cropsamarica.presentation.pest.pest_and_diseases.PestAndDiseasesScreen
import com.jmballangca.cropsamarica.presentation.profile.ProfileScreen

import com.jmballangca.cropsamarica.presentation.survey.SurveyScreen
import com.jmballangca.cropsamarica.presentation.task.TaskScreen
import com.jmballangca.cropsamarica.presentation.user_guide.UserGuideScreen
import com.jmballangca.cropsamarica.presentation.view_crop_field.ViewCropFieldScreen
import com.jmballangca.cropsamarica.presentation.weather_forecast.WeatherForecastScreen


@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    primaryNavController: NavHostController,
    navController: NavHostController,
    user: User,
    riceFields: List<RiceField>,
    onExpand: () -> Unit,
    notifications: List<Notifications>,

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
                notifications = notifications,
                user = user,
                primaryNavController = primaryNavController,
                onCreateCropField = {
                    navController.navigate(CREATE_CROP_FIELD)
                },
                onExpand = {
                    onExpand()
                },
                onProfileSelected = {
                    navController.navigate(PROFILE)
                }
            )
        }
        composable<TASK> {
            TaskScreen(
                riceFields = riceFields

            )
        }
        composable<PEST_AND_DISEASES> {
            PestAndDiseasesScreen(
                navController = navController
            )
        }

        composable<CREATE_CROP_FIELD>() {
            CreateCropFieldScreen(navController = navController)
        }


        composable<PEST_AND_DISEASES_DETAIL> {
            val id = it.toRoute<PEST_AND_DISEASES_DETAIL>()
            PestAndDiseaseDetailScreen(
                id = id.id,
                navController = navController,
            )
        }

        composable<USER_GUIDE> {
            UserGuideScreen(
                primaryNavController = navController
            )
        }
        composable<DEVELOPERS> {
            DeveloperScreen(
                primaryNavController = navController
            )
        }
        composable<WEATHER_FORECAST> {
            val id = it.toRoute<WEATHER_FORECAST>()
            WeatherForecastScreen(
                id = id.id,
                primaryNavController = navController
            )
        }
        composable<PROFILE> {
            ProfileScreen(
                primaryNavController = navController,
                navController = navController,
                onLogout = {
                    primaryNavController.navigate(ONBOARDING) {
                        launchSingleTop = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                }
            )
        }

        composable<VIEW_CROP_FIELD> {
            val id = it.toRoute<VIEW_CROP_FIELD>()
            ViewCropFieldScreen(
                id = id.id,
                navController = navController
            )
        }

        composable<NOTIFICATIONS> {

            NotificationScreen(
                notifications = notifications,
                navController = navController
            )
        }

        composable<VIEW_NOTIFICATION> {
            val id = it.toRoute<VIEW_NOTIFICATION>()
            ViewNotificationScreen(
                id = id.id,
                navController = navController
            )
        }


    }

}
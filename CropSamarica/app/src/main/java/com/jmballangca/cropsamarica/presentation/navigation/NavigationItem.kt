package com.jmballangca.cropsamarica.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PestControl
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PestControl
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Task
import androidx.compose.ui.graphics.vector.ImageVector
import com.jmballangca.cropsamarica.R


data class NavigationItem<T : Any>(
    val route: T,
    @StringRes val label: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean = false,
    val badgeCount: Int? = null
) {
    companion object {
         val MAIN_ROUTES  = listOf(
             NavigationItem(
                 route = HOME(),
                 label = R.string.home,
                 selectedIcon = Icons.Filled.Home,
                 unselectedIcon = Icons.Outlined.Home,
             ),
             NavigationItem(
                 route = PEST_AND_DISEASES,
                 label = R.string.pest_and_diseases,
                 selectedIcon = Icons.Filled.PestControl,
                 unselectedIcon = Icons.Outlined.PestControl,
             ),
             NavigationItem(
                 route = TASK,
                 label = R.string.task,
                 selectedIcon = Icons.Filled.Task,
                 unselectedIcon = Icons.Outlined.Task,
             ),
        )
    }
}
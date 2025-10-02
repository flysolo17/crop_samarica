package com.jmballangca.cropsamarica.presentation.navigation

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


data class NavigationItem<T : Any>(
    val route: T,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean = false,
    val badgeCount: Int? = null
) {
    companion object {
         val MAIN_ROUTES  = listOf(
             NavigationItem(
                 route = HOME(),
                 label = "Home",
                 selectedIcon = Icons.Filled.Home,
                 unselectedIcon = Icons.Outlined.Home,
             ),
             NavigationItem(
                 route = PEST_AND_DISEASES,
                 label = "Pest And Diseases",
                 selectedIcon = Icons.Filled.PestControl,
                 unselectedIcon = Icons.Outlined.PestControl,
             ),
             NavigationItem(
                 route = TASK,
                 label = "Task",
                 selectedIcon = Icons.Filled.Task,
                 unselectedIcon = Icons.Outlined.Task,
             ),
        )
    }
}
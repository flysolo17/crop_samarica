package com.jmballangca.cropsamarica.presentation.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jmballangca.cropsamarica.R
import com.jmballangca.cropsamarica.data.models.rice_field.RiceField
import com.jmballangca.cropsamarica.data.models.user.User
import com.jmballangca.cropsamarica.presentation.navigation.CHAT
import com.jmballangca.cropsamarica.presentation.navigation.CREATE_CROP_FIELD
import com.jmballangca.cropsamarica.presentation.navigation.HOME
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme
import com.jmballangca.cropsamarica.presentation.navigation.MainNavGraph
import com.jmballangca.cropsamarica.presentation.navigation.NavigationItem
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    primaryNavController: NavHostController,
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = hiltViewModel(),
) {
    val state by viewModel.mainState.collectAsStateWithLifecycle()
    val events = viewModel::events
    LaunchedEffect(state.selectedRiceField) {
        if (state.selectedRiceField != null) {
            navController.navigate(HOME(id = state.selectedRiceField!!.id)) {
                launchSingleTop = true
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            }
        }
    }
    when {
        state.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        !state.isLoading && state.user != null -> {

            MainScreen(
                modifier = modifier,
                user = state.user!!,
                riceFields = state.riceFields,
                selectedRiceField = state.selectedRiceField,
                primaryNavController = primaryNavController,
                navController = navController,
                onChange = {
                    viewModel.events(MainEvents.SelectRiceField(it))
                }
            )
        }

        else -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(text = "No user found")
            }
        }
    }

}

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    primaryNavController: NavHostController,
    navController: NavHostController,
    riceFields : List<RiceField>,
    user: User,
    selectedRiceField: RiceField?,
    onChange : (RiceField) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    riceFields.forEach {
                        NavigationDrawerItem(
                            label = {Text(it.name)},
                            selected = it.id == selectedRiceField?.id,
                            onClick = {
                                onChange(it)
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                        )
                    }
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                primaryNavController.navigate(CREATE_CROP_FIELD)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Create New"
                        )
                    }
                }

            }

        }
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {

            },
            bottomBar = {
                val items = NavigationItem.MAIN_ROUTES
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                    ) {
                        items.forEachIndexed { index , item ->
                            val selected = currentRoute?.hierarchy?.any {
                                it.hasRoute(item.route::class)
                            } == true
                            if (index == 1) {
                                FloatingActionButton(
                                    onClick = {
                                        primaryNavController.navigate(CHAT)
                                    },
                                    shape = CircleShape,
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.ai),
                                        contentDescription = "Add",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                            } else {
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            launchSingleTop = true
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (item.route == currentRoute?.route) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = item.label
                                        )
                                    },
                                    label = {
                                        Text(item.label)
                                    }
                                )
                            }

                        }
                    }
                }
            }
        ) {
            MainNavGraph(
                modifier = Modifier.padding(it),
                navController = navController,
                user = user,
                primaryNavController = primaryNavController,
                onExpand = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    }

}

@Preview
@Composable
private fun MainScreenPrev() {
    val conditions : List<String> = listOf("clear", "clouds", "rain", "thunderstorm", "snow", "mist", "fog", "haze")
    CropSamaricaTheme {
        MainScreen(
            primaryNavController = rememberNavController(),
            navController = rememberNavController(),
        )
    }
}
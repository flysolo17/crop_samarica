package com.jmballangca.cropsamarica.presentation.view_crop_field
import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp


@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LakbayCollapsingToolbar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    expandedContent : @Composable () -> Unit,
    title : @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable () -> Unit,
) {
    val maxImageHeight = 400.dp
    val minImageHeight = 0.dp
    val collapseFraction = scrollBehavior.state.collapsedFraction
    val imageHeightPx = with(LocalDensity.current) {
        lerp(
            maxImageHeight.toPx(),
            minImageHeight.toPx(),
            collapseFraction.coerceIn(0f, 1f)
        )
    }

    val imageHeight = with(LocalDensity.current) { imageHeightPx.toDp() }

    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight),
            contentAlignment = androidx.compose.ui.Alignment.BottomCenter
        ) {
            expandedContent()
        }

        LargeTopAppBar(
            expandedHeight = 400.dp,
            title = {
                if (scrollBehavior.state.collapsedFraction > 0.9f) {
                    title()
                }
            },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White,
                containerColor = Color.Transparent,
                scrolledContainerColor = MaterialTheme.colorScheme.surface
            ),
            navigationIcon = navigationIcon,
            actions = { actions() },
            scrollBehavior = scrollBehavior,
        )
    }
}
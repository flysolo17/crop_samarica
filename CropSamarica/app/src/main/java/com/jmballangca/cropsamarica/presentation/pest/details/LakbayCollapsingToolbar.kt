package com.jmballangca.cropsamarica.presentation.pest.details


import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import coil.compose.AsyncImage
import com.jmballangca.cropsamarica.R

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LakbayCollapsingToolbar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    images : List<String>,
    title : @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable () -> Unit,
) {
    val maxImageHeight = 250.dp
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
    val pagerState = rememberPagerState(
        initialPage = 0
    ){
        images.size
    }
    Box(
        modifier = modifier
    ) {
        LargeTopAppBar(
            expandedHeight = 250.dp,
            title = {
                if (collapseFraction >= 1f) {
                    title()
                }
            },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = Color.Transparent,
            ),
            navigationIcon = navigationIcon,
            actions = { actions() },
            scrollBehavior = scrollBehavior,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                AsyncImage(
                    model = images[page],
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.profile_bg),
                    error = painterResource(R.drawable.profile_bg),
                    contentDescription ="",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Box(
                modifier = Modifier
                    .padding(
                        top = 56.dp,
                        start = 16.dp
                    )
                    .background(
                        color = Color.Black.copy(
                            alpha = 0.5f
                        ),
                        shape = CircleShape
                    )
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                navigationIcon()
            }
            Text(
                "Page ${pagerState.currentPage + 1} of ${pagerState.pageCount}",
                modifier = Modifier.align(Alignment.BottomEnd).padding(
                    top = 12.dp,
                    end = 12.dp
                ),
                style = MaterialTheme.typography.bodySmall
            )
        }


    }
}
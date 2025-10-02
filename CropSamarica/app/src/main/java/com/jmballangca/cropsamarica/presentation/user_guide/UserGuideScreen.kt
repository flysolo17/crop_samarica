package com.jmballangca.cropsamarica.presentation.user_guide

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jmballangca.cropsamarica.core.ui.CollapsingToolbar
import com.jmballangca.cropsamarica.domain.models.UserGuide
import com.jmballangca.cropsamarica.presentation.developers.DeveloperItem
import com.jmballangca.cropsamarica.ui.theme.CropSamaricaTheme
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserGuideScreen(
    modifier: Modifier = Modifier,
    primaryNavController : NavController,
    viewModel: UserGuideViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    UserGuideScreen(
        isLoading = state.isLoading,
        userGuides = state.userGuides,
        error = state.error,
        onBackClick = {
            primaryNavController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserGuideScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    userGuides: List<UserGuide> = emptyList(),
    error: String? = null,
    onBackClick: () -> Unit
) {
    val nestedScrollConnection = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(nestedScrollConnection.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "User Guide")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            if (isLoading) {
                item {
                    LinearProgressIndicator()
                }
            }
            if (error != null) {
                item {
                    Text(text = error)
                }
            }
            items(userGuides.size) { index ->
                val userGuides = userGuides[index]
                UserGuideItem(
                    item = userGuides,
                ) { }
            }
        }

    }
}

@Composable
fun UserGuideItem(
    modifier: Modifier = Modifier,
    item : UserGuide,
    onClick : () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            if (!item.video.isNullOrEmpty()) {
                YoutubePlayer(youtubeVideoId = item.video.getVideoId())
            }
            Text(
                text = item.title.orEmpty(),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = item.description.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Preview
@Composable
private fun UserGuideItemPrev() {
    CropSamaricaTheme {
        UserGuideItem(
            modifier = Modifier.fillMaxWidth(),
            item = UserGuide(

                title = "Title",
                description = "Description",
                video = "https://www.youtube.com/watch?v=UNCggEPZQ0c"
            )
        ) {}
    }
}

fun String.getVideoId(): String {
    val videoId = Regex("v=([a-zA-Z0-9_-]{11})")
        .find(this)
        ?.groupValues?.get(1) ?: ""
    return videoId
}
@Composable
fun YoutubePlayer(
    youtubeVideoId: String
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // keep fixed height
            .clip(RoundedCornerShape(16.dp)),
        factory = { context ->
            YouTubePlayerView(context).apply {
                lifecycleOwner.lifecycle.addObserver(this)

                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        // Use cueVideo so it doesn’t auto-play
                        youTubePlayer.cueVideo(youtubeVideoId, 0f)
                    }
                })

                // Enable the fullscreen button (library handles it automatically)
                enableAutomaticInitialization = true
            }
        }
    )
}


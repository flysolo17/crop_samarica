package com.jmballangca.cropsamarica.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    name: String = "John Doe",
    profile : String,
    imageSize : Dp = 48.dp,
    onClick : () -> Unit = {}
) {
    Box(
        modifier = modifier.size(imageSize)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable {
                onClick()
            }
        ,
        contentAlignment = Alignment.Center
    ) {
        if (profile.isNotEmpty()) {
            AsyncImage(
                model = profile,
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Profile",
                contentScale = ContentScale.Crop
            )
        } else {
            Text(name.getOrElse(0, defaultValue = {'J'}).uppercase())
        }
    }
}
package com.jmballangca.cropsamarica.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.R
import com.jmballangca.cropsamarica.core.anim.BouncingArrow


@Composable
fun NoTaskFound(
    modifier: Modifier = Modifier,
    content : @Composable () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Image(
           painter = painterResource(R.drawable.no_task),
            contentDescription = "No Task Found",
            modifier = Modifier.size(
                120.dp
            )
        )
        Text(
            text = "No Task Found",
            modifier = Modifier.padding(16.dp)
        )
        BouncingArrow()
        Spacer(modifier = Modifier.height(8.dp))
        content()

    }
}
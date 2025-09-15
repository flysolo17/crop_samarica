package com.jmballangca.cropsamarica.presentation.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun AuthTab(
    modifier: Modifier = Modifier,
    currentPage: Int,
    selectedPage : Int,
    onTabSelected: (Int) -> Unit,
) {
    TabRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        selectedTabIndex = currentPage,
        containerColor = Color.Transparent, // remove default background
        contentColor = MaterialTheme.colorScheme.primary,
        indicator = { }, // remove default underline indicator
        divider = { }    // remove divider
    ) {
        listOf("Login", "Register").forEachIndexed { index, title ->
            Tab(
                selected = selectedPage == index,
                onClick = {
                   onTabSelected(index)
                },
                text = {
                    Text(
                        text = title,
                        color = if (selectedPage == index)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 8.dp)
                    .clip(MaterialTheme.shapes.extraLarge) // makes it pill-shaped
                    .background(
                        if (selectedPage == index)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Transparent
                    )
            )
        }
    }
}
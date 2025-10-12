package com.jmballangca.cropsamarica.ui.theme

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jmballangca.cropsamarica.R
import java.util.Locale
import androidx.core.content.edit

@SuppressLint("LocalContextConfigurationRead")
@Composable
fun MultiLanguageScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)


    var currentLang by remember {
        mutableStateOf(sharedPrefs.getString("language", "en") ?: "en")
    }
    val localizedContext = remember(currentLang) {
        context.createConfigurationContext(
            context.resources.configuration.apply {
                setLocale(
                    if (currentLang == "en") Locale("en", "US")
                    else Locale("tl", "PH")
                )
            }
        )
    }

    val resources = localizedContext.resources

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Current Language: $currentLang",
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = resources.getString(R.string.hello_world),
                modifier = Modifier.padding(16.dp)
            )

            Button(
                onClick = {
                    val newLang = if (currentLang == "en") "tl" else "en"
                    sharedPrefs.edit { putString("lang", newLang) }
                    currentLang = newLang
                }
            ) {
                Text(
                    text = if (currentLang == "en") "Translate to Tagalog" else "Translate to English"
                )
            }
        }
    }
}

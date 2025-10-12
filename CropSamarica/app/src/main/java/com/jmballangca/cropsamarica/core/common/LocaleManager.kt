package com.jmballangca.cropsamarica.core.common

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jmballangca.cropsamarica.data.models.pest.LocalizeText
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import java.util.Locale


interface LocaleManager {
    fun updateLocale(languageCode: String)
    fun getSavedLanguageCode(): Flow<String>

}

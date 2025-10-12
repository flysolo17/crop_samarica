package com.jmballangca.cropsamarica.core.common

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale
import androidx.core.content.edit

object LocaleHelper {
    fun wrapContext(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }
}
class LocaleManagerImpl @Inject constructor(
    private val context: Context
) : LocaleManager {

    private val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    private val languageFlow = MutableStateFlow(getInitialLanguageCode())

    private fun getInitialLanguageCode(): String {
        val currentLanguage = sharedPref.getString("language", "en") ?: "en"

        val locale = Locale(currentLanguage)
        Locale.setDefault(locale)

        context.createConfigurationContext(
            context.resources.configuration.apply {
                setLocale(locale)
            }
        )
        return currentLanguage
    }

    override fun updateLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        context.createConfigurationContext(
            context.resources.configuration.apply {
                setLocale(locale)
            }
        )
        sharedPref.edit { putString("language", languageCode) }
        languageFlow.value = languageCode
    }

    override fun getSavedLanguageCode(): Flow<String> {
        return languageFlow
    }
}
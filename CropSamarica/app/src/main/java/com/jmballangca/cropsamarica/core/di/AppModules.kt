package com.jmballangca.cropsamarica.core.di

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import androidx.credentials.CredentialManager
import androidx.room.Room

import com.google.firebase.Firebase
import com.google.firebase.ai.FirebaseAI
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerationConfig
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Tool
import com.google.firebase.ai.type.content
import com.jmballangca.cropsamarica.core.utils.SYSTEM_PROMPT
import com.jmballangca.cropsamarica.data.local.AppDatabase
import com.jmballangca.cropsamarica.data.local.dao.ConversationDao
import com.jmballangca.cropsamarica.data.local.dao.MessagesDao
import com.jmballangca.cropsamarica.data.service.WeatherApiService
import com.jmballangca.cropsamarica.domain.repository.impl.AyaRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModules {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Logs full request + response
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService
    = retrofit.create(WeatherApiService::class.java)

    @Provides
    @Singleton
    fun provideContext(
        @ApplicationContext context: Context
    ) = context


    @Provides
    @Singleton
    fun provideCredentialManager(
        @ApplicationContext context: Context
    ) = CredentialManager.create(context)

    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideConversationDao(
        db: AppDatabase
    ): ConversationDao = db.conversationDao()

    @Singleton
    @Provides
    fun provideMessagesDao(
        db: AppDatabase
    ): MessagesDao = db.messagesDao()

}
package com.jmballangca.cropsamarica.core.di

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.Tool
import com.google.firebase.ai.type.content
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jmballangca.cropsamarica.core.utils.CREATE_REMINDER
import com.jmballangca.cropsamarica.core.utils.SYSTEM_PROMPT
import com.jmballangca.cropsamarica.domain.repository.impl.AyaRepositoryImpl
import com.jmballangca.cropsamarica.domain.repository.impl.RiceFieldRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModules {

    @Provides
    @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideAuth() = FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun provideAyaAI() : GenerativeModel {
        val model = Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(
                modelName = "gemini-2.5-flash",
                systemInstruction = content {
                    text(SYSTEM_PROMPT)
                },
                tools = listOf(
                    Tool.functionDeclarations(
                        functionDeclarations = listOf(
                            AyaRepositoryImpl.CREATE_RICE_FIELD_DECLARATION,
                            RiceFieldRepositoryImpl.CREATE_ANNOUNCEMENT,
                            AyaRepositoryImpl.QUESTION_GENERATION_DECLARATION,
                            CREATE_REMINDER
                        )
                    )
                ),

                )
        return model
    }
}
package com.jmballangca.cropsamarica.core.di

import com.jmballangca.cropsamarica.domain.repository.AuthRepository
import com.jmballangca.cropsamarica.domain.repository.impl.AuthRepositoryImpl
import com.jmballangca.cropsamarica.domain.repository.AyaRepository
import com.jmballangca.cropsamarica.domain.repository.ConversationRepository
import com.jmballangca.cropsamarica.domain.repository.ConversationRepositoryImpl
import com.jmballangca.cropsamarica.domain.repository.ForecastRepository
import com.jmballangca.cropsamarica.domain.repository.RiceFieldRepository
import com.jmballangca.cropsamarica.domain.repository.TaskRepository
import com.jmballangca.cropsamarica.domain.repository.impl.TaskRepositoryImpl
import com.jmballangca.cropsamarica.domain.repository.impl.ForecastRepositoryImpl
import com.jmballangca.cropsamarica.domain.repository.impl.AyaRepositoryImpl
import com.jmballangca.cropsamarica.domain.repository.impl.RiceFieldRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAyaRepository(
        impl: AyaRepositoryImpl
    ): AyaRepository

    @Binds
    abstract fun bindForecastRepository(
        impl: ForecastRepositoryImpl
    ): ForecastRepository

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository


    @Binds
    abstract fun bindRiceFieldRepository(
        impl: RiceFieldRepositoryImpl
    ): RiceFieldRepository


    @Binds
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    abstract fun bindConversationRepository(
        impl: ConversationRepositoryImpl
    ): ConversationRepository

}

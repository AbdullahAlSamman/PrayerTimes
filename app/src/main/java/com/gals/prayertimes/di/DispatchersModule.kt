package com.gals.prayertimes.di

import com.gals.prayertimes.common.DefaultDispatcher
import com.gals.prayertimes.common.IODispatcher
import com.gals.prayertimes.common.MainDispatcher
import com.gals.prayertimes.common.TestDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @MainDispatcher
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @IODispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @TestDispatcher
    @Provides
    fun provideTestDispatcher(): CoroutineDispatcher = Dispatchers.Unconfined
}
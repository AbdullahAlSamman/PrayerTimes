package com.gals.prayertimes.di

import com.gals.prayertimes.model.DefaultDispatcher
import com.gals.prayertimes.model.IODispatcher
import com.gals.prayertimes.model.MainDispatcher
import com.gals.prayertimes.model.TestDispatcher
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
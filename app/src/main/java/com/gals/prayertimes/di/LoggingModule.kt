package com.gals.prayertimes.di

import com.gals.prayertimes.logging.AppDebugTree
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LoggingModule {

    @Provides
    fun provideAppDebugTree(): AppDebugTree = AppDebugTree("ngz")
}
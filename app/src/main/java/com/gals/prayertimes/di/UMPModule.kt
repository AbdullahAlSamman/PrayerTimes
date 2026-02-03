package com.gals.prayertimes.di

import android.content.Context
import com.google.android.ump.ConsentInformation
import com.google.android.ump.UserMessagingPlatform
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UMPModule {

    @Provides
    @Singleton
    fun provideConsentInformation(@ApplicationContext context: Context): ConsentInformation =
        UserMessagingPlatform.getConsentInformation(context)
}
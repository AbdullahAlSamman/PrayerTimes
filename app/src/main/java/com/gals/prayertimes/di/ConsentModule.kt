package com.gals.prayertimes.di

import android.content.Context
import com.gals.prayertimes.ads.manager.AdsConsentManager
import com.gals.prayertimes.ads.manager.ConsentManager
import com.google.android.ump.ConsentInformation
import com.google.android.ump.UserMessagingPlatform
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConsentModule {

    @Binds
    @Singleton
    abstract fun bindConsentManager(
        consentManagerImpl: AdsConsentManager
    ): ConsentManager

    companion object {
        @Provides
        @Singleton
        fun provideConsentInformation(@ApplicationContext context: Context): ConsentInformation =
            UserMessagingPlatform.getConsentInformation(context)
    }
}
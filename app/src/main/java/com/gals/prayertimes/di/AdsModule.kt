package com.gals.prayertimes.di

import com.gals.prayertimes.ads.AdsManager
import com.gals.prayertimes.ads.AdsManagerImpl
import com.gals.prayertimes.ads.consent.ConsentManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdsModule {

    @Provides
    @Singleton
    fun provideAdsManager(
        consentManager: ConsentManager
    ): AdsManager = AdsManagerImpl(consentManager)
}
package com.gals.prayertimes.di

import com.gals.prayertimes.ads.manager.AdsDebugConsentManager
import com.gals.prayertimes.ads.manager.ConsentManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConsentModule {

    @Binds
    @Singleton
    abstract fun bindConsentManager(
        adsDebugConsentManager: AdsDebugConsentManager
    ): ConsentManager
}
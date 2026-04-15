package com.gals.prayertimes.di

import com.gals.prayertimes.ads.AdsManager
import com.gals.prayertimes.ads.implementation.AdsManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AdsModule {

    @Binds
    @Singleton
    fun bindAdsManager(impl: AdsManagerImpl): AdsManager
}
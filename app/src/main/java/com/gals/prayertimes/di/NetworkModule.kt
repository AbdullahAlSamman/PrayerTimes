package com.gals.prayertimes.di

import com.gals.prayertimes.BuildConfig
import com.gals.prayertimes.repository.remote.PrayerService
import com.gals.prayertimes.repository.remote.PrayerTypeAdapter
import com.gals.prayertimes.repository.remote.model.PrayersResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideHttpClient(): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .callTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

    @Provides
    fun provideGsonFactory(): Gson =
        GsonBuilder().registerTypeAdapter(PrayersResponse::class.java, PrayerTypeAdapter()).create()

    @Provides
    fun provideRetrofitBuilder(
        httpClient: OkHttpClient.Builder,
        gsonFactory: Gson
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gsonFactory))
            .build()

    @Provides
    fun providePrayerService(
        retrofit: Retrofit
    ): PrayerService =
        retrofit.create(PrayerService::class.java)
}
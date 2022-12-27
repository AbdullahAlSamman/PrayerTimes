package com.gals.prayertimes.di

import com.gals.prayertimes.repository.remote.PrayerService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    private const val BASE_URL = "http://prayersapi.scienceontheweb.net/"

    @Provides
    fun provideHttpClient(): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .callTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)


    @Provides
    fun provideRetrofitBuilder(
        httpClient: OkHttpClient.Builder
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    fun providePrayerService(
        retrofit: Retrofit
    ): PrayerService =
        retrofit.create(PrayerService::class.java)

}
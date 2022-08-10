package com.gals.prayertimes.repository.network

import com.gals.prayertimes.repository.network.model.NetworkPrayer
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface PrayerService {
    @GET("api/prayer/{today_date}")
    suspend fun getTodayPrayer(@Path("today_date") date: String): Response<List<NetworkPrayer>>

    companion object {
        private var service: PrayerService? = null
        private val httpClient: OkHttpClient.Builder =
            OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)

        fun getInstance(): PrayerService? {
            if (service == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://prayersapi.scienceontheweb.net/")
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                service = retrofit.create(PrayerService::class.java)
            }
            return service
        }
    }
}
package com.gals.prayertimes.repository.network

import com.gals.prayertimes.repository.network.model.NetworkPrayer
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface PrayerService {
    @GET("api/prayer/{today_date}")
    suspend fun getTodayPrayer(@Path("today_date") date: String): Response<List<NetworkPrayer>>

    companion object {
        var service: PrayerService? = null

        fun getInstance(): PrayerService? {
            if (service == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://prayersapi.scienceontheweb.net/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                service = retrofit.create(PrayerService::class.java)
            }
            return service
        }
    }
}
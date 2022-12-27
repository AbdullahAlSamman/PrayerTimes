package com.gals.prayertimes.repository.remote

import com.gals.prayertimes.repository.remote.model.NetworkPrayer
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PrayerService {

    @GET("api/prayer/{today_date}")
    suspend fun getTodayPrayer(@Path("today_date") date: String): Response<List<NetworkPrayer>>

}
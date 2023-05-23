package com.gals.prayertimes.repository.remote

import com.gals.prayertimes.repository.remote.model.PrayerResponse
import com.gals.prayertimes.repository.remote.model.PrayersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PrayerService {

    @GET("api/prayer/{today_date}")
    suspend fun getTodayPrayer(@Path("today_date") date: String): Response<List<PrayerResponse>>

    @GET("api/prayer/nested/{today_date}")
    suspend fun getTodayPrayers(@Path("today_date") date: String) : Response<PrayersResponse>

}
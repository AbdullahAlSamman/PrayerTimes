package com.gals.prayertimes.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface PrayerService {
    @GET("api/prayer/{today_date}")
    fun getTodayPrayer(@Path("today_date") date: String): Call<List<NetworkPrayer>>
}

object PrayerServerData {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://prayersapi.scienceontheweb.net/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val prayer = retrofit.create(PrayerService::class.java)
}
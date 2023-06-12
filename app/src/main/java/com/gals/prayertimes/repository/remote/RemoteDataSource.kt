package com.gals.prayertimes.repository.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val prayerService: PrayerService
) {

    suspend fun getPrayer(todayDate: String) =
        prayerService.getTodayPrayer(todayDate)

    suspend fun getPrayers(todayDate: String) =
        prayerService.getTodayPrayers(todayDate)
}
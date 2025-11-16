package com.gals.prayertimes.repository.remote

import javax.inject.Inject

class PrayersRemoteDataSource @Inject constructor(
    private val prayerService: PrayerService
) {
    suspend fun getPrayers(todayDate: String) =
        prayerService.getTodayPrayers(todayDate)
}